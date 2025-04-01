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
import com.kazurayam.ks.testcase.ScriptsTraverser
import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testcase.TestCaseScriptsVisitor
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
	private Path scriptsDir // must not be null

	private ExtendedObjectRepository extOR
	private Database db

	private LocalDateTime startedAt
	private LocalDateTime finishedAt
	private int numberOfTestCases = 0
	private int numberOfTestObjects = 0

	/*
	 * 
	 */
	private ObjectRepositoryGarbageCollector(Builder builder) {
		this.objectRepositoryDir = builder.objectRepositoryDir.toAbsolutePath().normalize()
		this.scriptsDir = builder.scriptsDir.toAbsolutePath().normalize()
		this.db = new Database()
		this.extOR = builder.extendedObjectRepository
		startedAt = LocalDateTime.now()
		this.scan(this.db, extOR, this.scriptsDir)
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
	private void scan(Database db, ExtendedObjectRepository extOR, Path scriptsDir) {
		// scan the Object Repository directory to make a list of TestObjectEssences
		List<TestObjectEssence> essenceList = extOR.getTestObjectEssenceList("", false)
		//
		numberOfTestObjects = essenceList.size()

		// scan the Scripts directory to make a list of TestCaseIds
		TestCaseScriptsVisitor testCaseScriptsVisitor = new TestCaseScriptsVisitor(scriptsDir)
		Files.walkFileTree(scriptsDir, testCaseScriptsVisitor)
		List<TestCaseId> testCaseIdList = testCaseScriptsVisitor.getTestCaseIdList()
		//
		numberOfTestCases = testCaseIdList.size()

		// Iterate over the list of TestCaseIds.
		// Read the TestCase script, check if it contains any references to the TestObjects.
		// If true, record the reference into the database
		ScriptsTraverser scriptSearcher = new ScriptsTraverser(scriptsDir)
		testCaseIdList.each { testCaseId ->
			essenceList.each { essence ->
				TestObjectId testObjectId = essence.testObjectId()
				List<DigestedLine> textSearchResultList =
						scriptSearcher.digestTestCase(testCaseId, testObjectId.value(), false)
				textSearchResultList.each { textSearchResult ->
					ForwardReference reference = new ForwardReference(testCaseId, textSearchResult, essence)
					db.add(reference)
				}
			}
		}
	}

	Database db() {
		return db
	}

	int numberOfTestCases() {
		return numberOfTestCases
	}

	int numberOfTestObjects() {
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
	Garbages getGarbages() {
		Garbages garbages = new Garbages()
		// the allTestObjectIds variable is initialized by the scanSub() method
		extOR.allTestObjectIdSet.each { testObjectId ->
			Set<ForwardReference> value = db.findForwardReferencesTo(testObjectId)
			if (value == null) {
				// Oh, this TestObject must be a garbage
				// as no Test Case uses this
				garbages.add(testObjectId)
			}
		}
		return garbages
	}

	String jsonifyGarbages( ) {
		ObjectMapper mapper = new ObjectMapper()
		SimpleModule module = new SimpleModule("ObjectRepositoryGarbageCollectorSerializer",
				new Version(1, 0, 0, null, null, null))
		module.addSerializer(ObjectRepositoryGarbageCollector.class,
				new ObjectRepositoryGarbageCollector.ObjectRepositoryGarbageCollectorSerializer())
		module.addSerializer(ForwardReference.class,
				new ForwardReference.ForwardReferenceSerializer())
		module.addSerializer(TestCaseId.class,
				new TestCaseId.TestCaseIdSerializer())
		module.addSerializer(TestObjectEssence.class,
				new TestObjectEssence.TestObjectEssenceSerializer())
		module.addSerializer(TestObjectId.class,
				new TestObjectId.TestObjectIdSerializer())
		mapper.registerModule(module)
		return mapper.writeValueAsString(this)
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
			Path projectDir = gc.objectRepositoryDir.parent().normalize().toAbsolutePath()
			gen.writeStringField("Project name", projectDir.getFileName().toString())
			gen.writeNumberField("Number of TestCases", gc.numberOfTestCases())
			gen.writeNumberField("Number of TestObjects", gc.numberOfTestObjects())
			gen.writeNumberField("Duration seconds", gc.timeTaken())
			gen.writeFieldName("Unused TestObjects")
			gen.writeStartArray()
			Set<TestObjectId> toiSet = gc.getGarbages().getAllTestObjectIds()
			toiSet.each { TestObjectId toi ->
				gen.writeObject(toi)
			}
			gen.writeEndArray()
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
		private Path scriptsDir // non null

		private ExtendedObjectRepository extendedObjectRepository

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
			this.scriptsDir = scriptsDir
		}

		ObjectRepositoryGarbageCollector build() {
			extendedObjectRepository = new ExtendedObjectRepository(objectRepositoryDir)
			return new ObjectRepositoryGarbageCollector(this)
		}
	}
}
