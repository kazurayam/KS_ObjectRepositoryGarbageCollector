package com.kazurayam.ks.testobject.gc

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver
import com.kazurayam.ks.testcase.DigestedLine
import com.kazurayam.ks.testcase.ScriptsDecorator
import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testcase.TestCaseScriptDigester
import com.kazurayam.ks.testobject.LocatorIndex
import com.kazurayam.ks.testobject.ObjectRepositoryDecorator
import com.kazurayam.ks.testobject.TestObjectEssence
import com.kazurayam.ks.testobject.TestObjectId

import java.nio.file.Files
import java.nio.file.Path
import java.time.Duration
import java.time.LocalDateTime

/**
 * A sort of "Garbage Collector" for the "Object Repository" of a Katalon Studio project.
 * This class can lookup a list of unused Test Objects = "garbages".
 *
 * This class just compiles a report for you. It does not actually remove the unused TestObjects.
 *
 * @author kazurayam
 */
class ObjectRepositoryGarbageCollector {

	private Path objectRepositoryDir // must not be null
	private List<String> includeObjectRepositoryFolder  // could be empty

	private Path scriptsDir // must not be null
	private List<String> includeScriptsFolder // could be empty

	private Database db
	private ObjectRepositoryDecorator ord

	private LocalDateTime startedAt
	private LocalDateTime finishedAt
	private int numberOfTestCases = 0
	private int numberOfTestObjects = 0

	/*
	 * 
	 */
	private ObjectRepositoryGarbageCollector(Builder builder) {
		objectRepositoryDir = builder.objectRepositoryDir.toAbsolutePath().normalize()
		includeObjectRepositoryFolder = builder.includeObjectRepositoryFolder
		//
		scriptsDir = builder.scriptsDir.toAbsolutePath().normalize()
		includeScriptsFolder = builder.includeScriptsFolder
		//
		startedAt = LocalDateTime.now()
		def recv = this.scan(this.objectRepositoryDir, this.scriptsDir)
		this.db = recv[0]
		this.ord = recv[1]
		finishedAt = LocalDateTime.now()
	}

	/*
	 * This method will scan the "Object Repository" folder and the "Scripts" folder.
	 * to create an instance of Database internally and fill it with information found
	 * out of the directories.
	 * You can retrieve the Database by calling "db()" method.
	 * You can retrieve an Garbage Collection plan by calling "xref()" method, in which you can
	 * find a list of "garbage" Test Objects which are not used by any of the Test Cases.
	 */
	private def scan(Path objectRepositoryDir, Path scriptsDir) {

		Database db = new Database()

		ObjectRepositoryDecorator ord =
				new ObjectRepositoryDecorator.Builder(objectRepositoryDir)
				.includeFolder(this.includeObjectRepositoryFolder)
				.build()

		// scan the Object Repository directory to make a list of TestObjectEssences
		List<TestObjectEssence> essenceList = ord.getTestObjectEssenceList("", false)
		//
		numberOfTestObjects = essenceList.size()

		// scan the Scripts directory to make a list of TestCaseIds
		ScriptsDecorator scriptsDecorator =
				new ScriptsDecorator.Builder(scriptsDir)
				.includeFolder(includeScriptsFolder)
				.build()
		List<TestCaseId> testCaseIdList = getTestCaseIdList(scriptsDir, scriptsDecorator.getGroovyFiles())

		//
		numberOfTestCases = testCaseIdList.size()

		// Iterate over the list of TestCaseIds.
		// Read the TestCase script, check if it contains any references to the TestObjects.
		// If true, record the reference into the database
		TestCaseScriptDigester scriptTraverser = new TestCaseScriptDigester(scriptsDir)
		testCaseIdList.each { testCaseId ->
			essenceList.each { essence ->
				TestObjectId testObjectId = essence.getTestObjectId()
				List<DigestedLine> digestedLines = scriptTraverser.digestTestCase(testCaseId, testObjectId.getValue(), false)
				digestedLines.each { digestedLine ->
					ForwardReference reference = new ForwardReference(testCaseId, digestedLine, essence)
					db.add(reference)
				}
			}
		}
		return [db, ord]
	}


	private List<TestCaseId> getTestCaseIdList(Path scriptsDir, List<Path> groovyFiles) {
		List<TestCaseId> list = new ArrayList<>()
		groovyFiles.forEach ({ groovyFile ->
			TestCaseId id = TestCaseId.resolveTestCaseId(scriptsDir, groovyFile)
			list.add(id)
		})
		return list
	}


	Database db() {
		return db
	}

	/**
	 *
	 */
	String jsonifyDatabase() {
		return db.toJson()
	}


	Path getProjectDir() {
		return this.objectRepositoryDir.getParent().normalize().toAbsolutePath()
	}

	List<String> getIncludeObjectRepositoryFolder() {
		return includeObjectRepositoryFolder
	}

	List<String> getIncludeScriptsFolder() {
		return includeScriptsFolder
	}

	int getNumberOfTestCases() {
		return numberOfTestCases
	}

	int getNumberOfTestObjects() {
		return numberOfTestObjects
	}

	/**
	 *
	 */
	BackwardReferences getBackwardReferences() {
		BackwardReferences backwardReferences = new BackwardReferences()
		Set<TestObjectId> allTestObjectIds = db.getAllTestObjectIdsContained()
		allTestObjectIds.each { testObjectId ->
			Set<ForwardReference> forwardReferences = db.findForwardReferencesTo(testObjectId)
			forwardReferences.each { ForwardReference fr ->
				backwardReferences.put(testObjectId, fr)
			}
		}
		return backwardReferences
	}

	/**
	 *
	 */
	String jsonifyBackwardReferences() {
		BackwardReferences backwardReferences = this.getBackwardReferences()
		return backwardReferences.toJson()
	}

	/**
	 * 
	 */
	LocatorIndex getLocatorIndex(String pattern = "", boolean isRegex = false) {
		return ord.getLocatorIndex(pattern, isRegex)
	}

	/**
	 * 
	 */
	String jsonifyLocatorIndex(String pattern = "", boolean isRegex = false) {
		return ord.jsonifyLocatorIndex(pattern, isRegex)
	}

	/**
	 * generate a Garbage object, which contains a list of the unused TestObject Id.
	 */
	Garbages getGarbages() {
		Garbages garbages = new Garbages()
		//println "ord.getAllTestObjectIdSet().size()=" + ord.getAllTestObjectIdSet().size()
		this.ord.getAllTestObjectIdSet().each { testObjectId ->
			Set<ForwardReference> forwardReferences = db.findForwardReferencesTo(testObjectId)
			//println "testObjectId=" + testObjectId.getValue() + " forwardReferences.size()=" + forwardReferences.size()
			if (forwardReferences.size() == 0) {
				// Oh, no TestCase uses this TestObject, this TestObject is unused
				garbages.add(testObjectId)
			}
		}
		return garbages
	}

	String jsonifyGarbages( ) {
		SimpleModule module = new SimpleModule("ObjectRepositoryGarbageCollectorSerializer",
				new Version(1, 0, 0, null, null, null))

		module.addSerializer(ObjectRepositoryGarbageCollector.class,
				new ObjectRepositoryGarbageCollector.ObjectRepositoryGarbageCollectorSerializer())

		module.addSerializer(Garbages.class,
				new Garbages.GarbagesSerializer())

		module.addSerializer(ForwardReference.class,
				new ForwardReference.ForwardReferenceSerializer())

		module.addSerializer(TestCaseId.class,
				new TestCaseId.TestCaseIdSerializer())

		module.addSerializer(TestObjectEssence.class,
				new TestObjectEssence.TestObjectEssenceSerializer())

		module.addSerializer(TestObjectId.class,
				new TestObjectId.TestObjectIdSerializer())

		ObjectMapper mapper = new ObjectMapper()
		mapper.registerModule(module)
		return mapper.writeValueAsString( this )
	}

	static class ObjectRepositoryGarbageCollectorSerializer extends StdSerializer<ObjectRepositoryGarbageCollector> {
		ObjectRepositoryGarbageCollectorSerializer() {
			this(null)
		}
		ObjectRepositoryGarbageCollectorSerializer(Class<ObjectRepositoryGarbageCollector> t) {
			super(t)
		}
		@Override
		void serialize(ObjectRepositoryGarbageCollector gc,
				JsonGenerator gen, SerializerProvider serializer) {
			gen.writeStartObject()
			gen.writeStringField("Project name", gc.getProjectDir().getFileName().toString())
			if (!gc.getIncludeScriptsFolder().isEmpty()) {
				gen.writeFieldName("includeScriptsFolder")
				gen.writeStartArray()
				List<String> patterns = gc.getIncludeScriptsFolder()
				patterns.each { ptrn ->
					gen.writeString(ptrn)
				}
				gen.writeEndArray()
			}
			if (!gc.getIncludeObjectRepositoryFolder().isEmpty()) {
				gen.writeFieldName("includeObjectRepositoryFolder")
				gen.writeStartArray()
				List<String> patterns = gc.getIncludeObjectRepositoryFolder()
				patterns.each { ptrn ->
					gen.writeString(ptrn)
				}
				gen.writeEndArray()
			}
			gen.writeNumberField("Number of TestCases", gc.getNumberOfTestCases())
			gen.writeNumberField("Number of TestObjects", gc.getNumberOfTestObjects())
			gen.writeNumberField("Number of unused TestObjects", gc.getGarbages().size())
			gen.writeFieldName("Unused TestObjects")
			gen.writeStartArray()
			Set<TestObjectId> toiSet = gc.getGarbages().getAllTestObjectIds()
			toiSet.each { TestObjectId toi ->
				gen.writeString(toi.getValue())
			}
			gen.writeEndArray()
			gen.writeNumberField("Duration seconds", gc.timeTaken())
			gen.writeEndObject()
		}
	}

	Double timeTaken() {
		Duration timeTaken = Duration.between(startedAt, finishedAt)
		return toSeconds(timeTaken)
	}

	private Double toSeconds(Duration dur) {
		Double v = dur.toMillis() / 1000
		return v
	}


	/**
	 * Joshua Bloch's Builder pattern in Effective Java
	 *
	 * @author kazuarayam
	 */
	public static class Builder {

		private Path objectRepositoryDir // non null
		private List<String> includeObjectRepositoryFolder  // sub-folders in the "Object Repository" directory, may be empty

		private Path scriptsDir // non null
		private List<String> includeScriptsFolder // sub-folders in the "Scripts" directory, may be empty


		Builder() {
			Path projectDir = KatalonProjectDirectoryResolver.getProjectDir().toAbsolutePath().normalize()
			Path objrepoDir = projectDir.resolve("Object Repository")
			Path scriptsDir = projectDir.resolve("Scripts")
			init(objrepoDir, scriptsDir)
		}

		Builder(Path objectRepositoryDir, Path scriptsDir) {
			init(objectRepositoryDir, scriptsDir)
		}

		Builder(File objectRepositoryDir, File scriptsDir) {
			init(objectRepositoryDir.toPath(), scriptsDir.toPath())
		}

		private void init(Path objectRepositoryDir, Path scriptsDir) {
			Objects.requireNonNull(objectRepositoryDir)
			Objects.requireNonNull(scriptsDir)
			assert Files.exists(objectRepositoryDir)
			assert Files.exists(scriptsDir)
			this.objectRepositoryDir = objectRepositoryDir
			this.includeObjectRepositoryFolder = new ArrayList<>()
			this.scriptsDir = scriptsDir
			this.includeScriptsFolder = new ArrayList()
		}

		/**
		 * 
		 * @param subpaths expressions like Ant FileSet pattern, e.g. 
		 * - <code>.includeObjectRepsitoryFolder("main/Page_CURA Healthcare Service")</code>
		 * - <code>.includeObjectRepositoryFolder("main/Page_CURA Healthcare Service2")</code>
		 * - <code>.includeObjectRepositoryFolder("main/Page_CURA Healthcare Service?")</code>
		 * - <code>.includeObjectRepositoryFolder("main/Page_CURA*")</code>
		 * - <code>.includeObjectRepositoryFolder("**\\/Page_CURA*")</code>
		 */
		Builder includeObjectRepositoryFolder(String pattern) {
			Objects.requireNonNull(pattern)
			includeObjectRepositoryFolder.add(pattern)
			return this
		}

		Builder includeObjectRepositoryFolder(List<String> pattern) {
			Objects.requireNonNull(pattern)
			includeObjectRepositoryFolder.addAll(pattern)
			return this
		}

		Builder includeScriptsFolder(String pattern) {
			Objects.requireNonNull(pattern)
			includeScriptsFolder.add(pattern)
			return this
		}

		Builder includeScriptsFolder(List<String> pattern) {
			Objects.requireNonNull(pattern)
			includeScriptsFolder.addAll(pattern)
			return this
		}


		public ObjectRepositoryGarbageCollector build() {
			return new ObjectRepositoryGarbageCollector(this)
		}
	}
}
