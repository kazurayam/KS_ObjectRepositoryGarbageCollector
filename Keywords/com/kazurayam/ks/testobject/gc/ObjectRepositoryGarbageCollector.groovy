package com.kazurayam.ks.testobject.gc

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Duration
import java.time.LocalDateTime

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.kazurayam.ks.testcase.DigestedLine
import com.kazurayam.ks.testcase.ScriptDigester
import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testcase.ScriptsVisitor
import com.kazurayam.ks.testobject.ExtendedObjectRepository
import com.kazurayam.ks.testobject.TestObjectEssence
import com.kazurayam.ks.testobject.TestObjectId
import com.kms.katalon.core.configuration.RunConfiguration

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
	private List<String> objectRepositorySubpaths  // could be empty
	private Path scriptsDir // must not be null
	private List<String> scriptsSubpaths // could be empty

	private Database db
	private ExtendedObjectRepository extOR

	private LocalDateTime startedAt
	private LocalDateTime finishedAt
	private int numberOfTestCases = 0
	private int numberOfTestObjects = 0

	/*
	 * 
	 */
	private ObjectRepositoryGarbageCollector(Builder builder) {
		this.objectRepositoryDir = builder.objectRepositoryDir.toAbsolutePath().normalize()
		this.objectRepositorySubpaths = builder.objectRepositorySubpaths
		this.scriptsDir = builder.scriptsDir.toAbsolutePath().normalize()
		this.scriptsSubpaths = builder.scriptsSubpaths
		//
		startedAt = LocalDateTime.now()
		def recv = this.scan(this.objectRepositoryDir, this.scriptsDir)
		this.db = recv[0]
		this.extOR = recv[1]
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
		ExtendedObjectRepository extOR = new ExtendedObjectRepository(objectRepositoryDir)

		// scan the Object Repository directory to make a list of TestObjectEssences
		List<TestObjectEssence> essenceList = extOR.getTestObjectEssenceList("", false)
		//
		numberOfTestObjects = essenceList.size()

		// scan the Scripts directory to make a list of TestCaseIds
		ScriptsVisitor scriptsVisitor = new ScriptsVisitor(scriptsDir)
		Files.walkFileTree(scriptsDir, scriptsVisitor)
		List<TestCaseId> testCaseIdList = scriptsVisitor.getTestCaseIdList()
		//
		numberOfTestCases = testCaseIdList.size()

		// Iterate over the list of TestCaseIds.
		// Read the TestCase script, check if it contains any references to the TestObjects.
		// If true, record the reference into the database
		ScriptDigester scriptTraverser = new ScriptDigester(scriptsDir)
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
		return [db, extOR]
	}

	Database db() {
		return db
	}

	Path getProjectDir() {
		return this.objectRepositoryDir.getParent().normalize().toAbsolutePath()
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
	 * generate a Garbage object, which contains a list of the unused TestObject Id.
	 */
	Garbages getGarbages() {
		Garbages garbages = new Garbages()
		//println "extOR.getAllTestObjectIdSet().size()=" + extOR.getAllTestObjectIdSet().size()
		this.extOR.getAllTestObjectIdSet().each { testObjectId ->
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
		private List<String> objectRepositorySubpaths  // could be empty
		private Path scriptsDir // non null
		private List<String> scriptsSubpaths  // could be empty

		Builder() {
			Path projectDir = Paths.get(RunConfiguration.getProjectDir()).toAbsolutePath().normalize()
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
			this.objectRepositorySubpaths = new ArrayList<>()
			this.scriptsDir = scriptsDir
			this.scriptsSubpaths = new ArrayList<>()
		}

		/**
		 * 
		 * @param subpaths expressions like Ant FileSet pattern, e.g. 
		 * - <code>.objectRepository("Page_CURA Healthcare Service")</code>
		 * - <code>.objectRepository("Page_CURA Healthcare Service*")</code>
		 * - <code>.objectRepository('** /input_*')</code>
		 * - <code>.objectRepository('** /a_*', '** /button_*')</code>
		 * - <code>.objectRepository(['** /a_*', '** /button_*'])</code>
		 */
		Builder objectRepositorySubpaths(String... subpaths) {
			Objects.requireNonNull(subpaths)
			(subpaths as List).each { subpath ->
				Path p = objectRepositoryDir.resolve(subpath)
				this.objectRepositorySubpaths.add(subpath)
			}
			return this
		}

		Builder scriptsSubpaths(String... subpaths) {
			Objects.requireNonNull(subpaths)
			(subpaths as List).each { subpath ->
				Path p = scriptsDir.resolve(subpath)
				this.scriptsSubpaths.add(subpath)
			}
			return this
		}

		public ObjectRepositoryGarbageCollector build() {
			return new ObjectRepositoryGarbageCollector(this)
		}
	}
}
