package com.kazurayam.ks.testobject.combine

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
import com.kazurayam.ks.testobject.Locator
import com.kazurayam.ks.testobject.ObjectRepositoryDecorator
import com.kazurayam.ks.testobject.TestObjectId

import java.nio.file.Files
import java.nio.file.Path

/**
 * A sort of "Garbage Collector" for the "Object Repository" of a Katalon Studio project.
 * This class can lookup a list of unused Test Objects = "garbage".
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

	private ForwardReferences forwardReferences
	private ObjectRepositoryDecorator ord
	private BackwardReferenceIndex backwardReferenceIndex

	private int numberOfTestCases = 0
	private int numberOfTestObjects = 0

	private RunDescription runDescription

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
		def recv = this.scan(this.objectRepositoryDir, this.scriptsDir)
		this.forwardReferences = (ForwardReferences)recv[0]
		this.ord = (ObjectRepositoryDecorator)recv[1]
		this.backwardReferenceIndex = this.getBackwardReferenceIndex()
		String projectName = this.getProjectDir().getFileName().toString()
		this.runDescription =
				new RunDescription.Builder(projectName)
						.includeScriptsFolder(this.includeScriptsFolder)
						.includeObjectRepositoryFolder(this.includeObjectRepositoryFolder)
						.numberOfTestCases(this.getNumberOfTestCases())
						.numberOfTestObjects(this.getNumberOfTestObjects())
						.numberOfUnusedTestObjects(this.getGarbage().size())
						.build()
	}

	/*
	 * This method will scan the "Object Repository" folder and the "Scripts" folder.
	 * to create an instance of ForwardReferences internally and fill it with information found
	 * out of the directories.
	 * You can retrieve the ForwardReferences by calling "db()" method.
	 * You can retrieve an Garbage Collection plan by calling "getGarbage()" method, in which you can
	 * find a list of "garbage" Test Objects which are not used by any of the Test Cases.
	 */
	private def scan(Path objectRepositoryDir, Path scriptsDir) {

		ForwardReferences db = new ForwardReferences()

		ObjectRepositoryDecorator ord =
				new ObjectRepositoryDecorator.Builder(objectRepositoryDir)
				.includeFolder(this.includeObjectRepositoryFolder)
				.build()

		List<TestObjectId> testObjectIdList = ord.getTestObjectIdList("", false)
		//
		numberOfTestObjects = testObjectIdList.size()

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
			testObjectIdList.each { testObjectId ->
				List<DigestedLine> digestedLines = scriptTraverser.digestTestCase(testCaseId, testObjectId.getValue(), false)
				digestedLines.each { digestedLine ->
					ForwardReference reference = new ForwardReference(testCaseId, digestedLine, testObjectId)
					db.add(reference)
				}
			}
		}
		return [db, ord]
	}

	private static List<TestCaseId> getTestCaseIdList(Path scriptsDir, List<Path> groovyFiles) {
		List<TestCaseId> list = new ArrayList<>()
		groovyFiles.forEach ({ groovyFile ->
			TestCaseId id = TestCaseId.resolveTestCaseId(scriptsDir, groovyFile)
			list.add(id)
		})
		return list
	}

	ForwardReferences getForwardReferences() {
		return forwardReferences
	}

	/**
	 *
	 */
	String jsonifyDatabase() {
		return forwardReferences.toJson()
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
	BackwardReferenceIndex getBackwardReferenceIndex() {
		BackwardReferenceIndex index = new BackwardReferenceIndex()
		Set<TestObjectId> allTestObjectIds = forwardReferences.getAllTestObjectIdsContained()
		allTestObjectIds.each { testObjectId ->
			BackwardReference br = new BackwardReference(testObjectId)
			Set<ForwardReference> forwardReferences =
					forwardReferences.findForwardReferencesTo(testObjectId)
			if (forwardReferences != null) {
				forwardReferences.each { fr ->
					br.add(fr)
				}
			}
			index.put(testObjectId, br)
		}
		return index
	}

	/**
	 *
	 */
	String jsonifyBackwardReferenceIndex() {
		BackwardReferenceIndex index =
				this.getBackwardReferenceIndex()
		return index.toJson()
	}

	//-----------------------------------------------------------------

	/**
	 * generate a Garbage object, which contains a list of the unused TestObject Id.
	 */
	Garbage getGarbage() {
		Garbage garbage = new Garbage()
		//println "ord.getAllTestObjectIdSet().size()=" + ord.getAllTestObjectIdSet().size()
		this.ord.getAllTestObjectIdSet().each { testObjectId ->
			Set<ForwardReference> forwardReferences = forwardReferences.findForwardReferencesTo(testObjectId)
			//println "testObjectId=" + testObjectId.getValue() + " forwardReferences.size()=" + forwardReferences.size()
			if (forwardReferences.size() == 0) {
				// Oh, no TestCase uses this TestObject, this TestObject is unused
				garbage.add(testObjectId)
			}
		}
		return garbage
	}

	String jsonifyGarbage( ) {
		SimpleModule module = new SimpleModule("GarbageSerializer",
				new Version(1, 0, 0, null, null, null))

		module.addSerializer(ObjectRepositoryGarbageCollector.class,
				new GarbageSerializer())

		module.addSerializer(RunDescription.class,
				new RunDescription.RunDescriptionSerializer())

		module.addSerializer(Garbage.class,
				new Garbage.GarbageSerializer())

		module.addSerializer(ForwardReference.class,
				new ForwardReference.ForwardReferenceSerializer())

		module.addSerializer(TestCaseId.class,
				new TestCaseId.TestCaseIdSerializer())

		module.addSerializer(TestObjectId.class,
				new TestObjectId.TestObjectIdSerializer())

		ObjectMapper mapper = new ObjectMapper()
		mapper.registerModule(module)
		return mapper.writeValueAsString( this )
	}

	/**
	 *
	 */
	static class GarbageSerializer extends StdSerializer<ObjectRepositoryGarbageCollector> {
		GarbageSerializer() {
			this(null)
		}

		GarbageSerializer(Class<ObjectRepositoryGarbageCollector> t) {
			super(t)
		}
		@Override
		void serialize(ObjectRepositoryGarbageCollector gc,
					   JsonGenerator gen, SerializerProvider serializer) {
			gen.writeStartObject()
			gen.writeFieldName("Garbage")
			gen.writeStartArray()
			Set<TestObjectId> toiSet = gc.getGarbage().getAllTestObjectIds()
			toiSet.each { TestObjectId toi ->
				gen.writeString(toi.getValue())
			}
			gen.writeEndArray()
			gen.writeObjectField("Run Description", gc.runDescription)
			gen.writeEndObject()
		}
	}

	//-----------------------------------------------------------------

	/**
	 *
	 */
	CombinedLocatorIndex getCombinedLocatorIndex() {
		Set<Locator> locatorSet = new TreeSet<>()
		// create a set of all Locators found in the Object Repository directory
		ord.getTestObjectIdList().each { toi ->
			// find the Locator that this Test Object declares
			Locator locator = ord.getLocator(toi)
			locatorSet.add(locator)
		}
		CombinedLocatorIndex clx = new CombinedLocatorIndex()
		locatorSet.each { locator ->
			Set<TestObjectId> containers = ord.findTestObjectsWithLocator(locator)
			containers.each { toi ->
				CombinedLocatorDeclarations declarations = new CombinedLocatorDeclarations(toi)
				Set<BackwardReference> backwardReferences = backwardReferenceIndex.get(toi)
				backwardReferences.each { br ->
					declarations.add(br)
				}
				clx.put(locator, declarations)
			}
		}
		return clx
	}

	String jsonifyCombinedLocatorIndex() {
		SimpleModule module = new SimpleModule("jsonifyCombinedLocatorIndex",
				new Version(1, 0, 0, null, null, null))
		module.addSerializer(ObjectRepositoryGarbageCollector.class,
				new CombinedLocatorIndexSerializer())
		addComponentSerializers(module)
		ObjectMapper mapper = new ObjectMapper()
		mapper.registerModule(module)
		return mapper.writeValueAsString( this )
	}

	/**
	 * The most useful feature for users
	 */
	String jsonifySuspiciousLocatorIndex() {
		SimpleModule module = new SimpleModule("jsonifySuspiciousLocatorIndex",
				new Version(1, 0, 0, null, null, null))
		module.addSerializer(ObjectRepositoryGarbageCollector.class,
				new SuspiciousLocatorIndexSerializer())
		addComponentSerializers(module)
		ObjectMapper mapper = new ObjectMapper()
		mapper.registerModule(module)
		return mapper.writeValueAsString( this )
	}

	private static void addComponentSerializers(SimpleModule module) {
		module.addSerializer(CombinedLocatorIndex.class,
				new CombinedLocatorIndex.CombinedLocatorIndexSerializer())
		module.addSerializer(RunDescription.class,
				new RunDescription.RunDescriptionSerializer())
		module.addSerializer(ForwardReference.class,
				new ForwardReference.ForwardReferenceSerializer())
		module.addSerializer(TestCaseId.class,
				new TestCaseId.TestCaseIdSerializer())
		module.addSerializer(TestObjectId.class,
				new TestObjectId.TestObjectIdSerializer())
	}

	static class CombinedLocatorIndexSerializer
			extends StdSerializer<ObjectRepositoryGarbageCollector> {
		CombinedLocatorIndexSerializer() {
			this(null)
		}
		CombinedLocatorIndexSerializer(Class<ObjectRepositoryGarbageCollector> t) {
			super(t)
		}
		@Override
		void serialize(ObjectRepositoryGarbageCollector gc,
					   JsonGenerator gen, SerializerProvider serializer) {
			gen.writeStartObject()
			gen.writeObjectField("CombinedLocatorIndex", gc.combinedLocatorIndex)
			gen.writeObjectField("Run Description", gc.runDescription)
			gen.writeEndObject()
		}
	}

	static class SuspiciousLocatorIndexSerializer
			extends StdSerializer<ObjectRepositoryGarbageCollector> {
		SuspiciousLocatorIndexSerializer() {
			this(null)
		}
		SuspiciousLocatorIndexSerializer(Class<ObjectRepositoryGarbageCollector> t) {
			super(t)
		}
		@Override
		void serialize(ObjectRepositoryGarbageCollector gc,
					   JsonGenerator gen, SerializerProvider serializer) {
			gen.writeStartObject()
			gen.writeObjectField("SuspiciousLocatorIndex",
					CombinedLocatorIndex.suspect(gc.combinedLocatorIndex))
			gen.writeObjectField("Run Description", gc.runDescription)
			gen.writeEndObject()
		}
	}

	//-----------------------------------------------------------------

	/**
	 * Joshua Bloch's Builder pattern in Effective Java
	 *
	 * @author kazuarayam
	 */
	static class Builder {

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


		ObjectRepositoryGarbageCollector build() {
			return new ObjectRepositoryGarbageCollector(this)
		}
	}
}
