package com.kazurayam.ks.testobject.gc

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.Duration

import com.kazurayam.ks.testcase.ScriptsTraverser
import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testcase.TestCaseScriptsVisitor
import com.kazurayam.ks.testcase.DigestedLine
import com.kazurayam.ks.testobject.ExtendedObjectRepository
import com.kazurayam.ks.testobject.TestObjectEssence
import com.kazurayam.ks.testobject.TestObjectId
import com.kms.katalon.core.configuration.RunConfiguration
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import groovy.json.JsonOutput


/**
 * A sort of "Garbage Collector" for the "Object Repository" of a Katalon Studio project.
 * This class can lookup a list of unused Test Objects = "garbages".
 *
 * This class just compiles a report for you. It does not actually remove the unused TestObjects.
 *
 * @author kazurayam
 */
class ObjectRepositoryGarbageCollector {

	private static final projectDir = Paths.get(".").normalize().toAbsolutePath()

	private Path objectRepositoryDir // must not be null
	private Path testCasesDir // must not be null
	private List<String> objectRepositorySubpaths // must not be null but could be empty
	private List<String> testCasesSubpaths // must not be null but could be empty

	private ExtendedObjectRepository extOR
	private Database db
	private Set<TestObjectId> allTestObjectIds

	private ObjectRepositoryGarbageCollector(Builder builder) {
		this.objectRepositoryDir = builder.objectRepositoryDir.toAbsolutePath().normalize()
		this.testCasesDir = builder.testCasesDir.toAbsolutePath().normalize()
		this.objectRepositorySubpaths = builder.objectRepositorySubpaths
		this.testCasesSubpaths = builder.testCasesSubpaths
		extOR = new ExtendedObjectRepository(objectRepositoryDir, objectRepositorySubpaths)
		this.scan()
	}

	private LocalDateTime startedAt
	private LocalDateTime finishedAt
	private int numberOfTestCases = 0
	private int numberOfTestObjects = 0

	/*
	 * This method will scan the "Object Repository" folder and the "Scripts" folder.
	 * to create an instance of Database internally and fill it with information found
	 * out of the directories.
	 * You can retrieve the Database by calling "db()" method.
	 * You can retrieve an Garbage Collection plan by calling "xref()" method, in which you can
	 * find a list of "garbage" Test Objects which are not used by any of the Test Cases.
	 */
	public void scan(ExtendedObjectRepository extOR) {
		this.db = new Database()
		startedAt = LocalDateTime.now()
		objectRepositorySubpaths.each { objectRepositorySubpath ->
			testCasesSubpaths.each { testCasesSubpath ->
				scanSub(this.db, 
					extOR,
					this.testCasesDir, 
					testCasesSubpath)
			}
		}
		finishedAt = LocalDateTime.now()
	}

	private void scanSub(Database db,
							ExtendedObjectRepository extOR,
							Path scriptsDir,
							String testCasesSubpath) {
		// scan the Object Repository directory to make a list of TestObjectEssences
		allTestObjectIds = extOR.getAllTestObjectIdSet()
		List<TestObjectEssence> essenceList = extOR.getTestObjectEssenceList("", false)
		//
		numberOfTestObjects = essenceList.size()
		
		// scan the Scripts directory to make a list of TestCaseIds
		TestCaseScriptsVisitor testCaseScriptsVisitor = new TestCaseScriptsVisitor(scriptsDir)
		Path targetDir = (testCasesSubpath != null) ? scriptsDir.resolve(testCasesSubpath) : scriptsDir
		Files.walkFileTree(targetDir, testCaseScriptsVisitor)
		List<TestCaseId> testCaseIdList = testCaseScriptsVisitor.getTestCaseIdList()
		//
		numberOfTestCases = testCaseIdList.size()

		// Iterate over the list of TestCaseIds.
		// Read the TestCase script, check if it contains any references to the TestObjects.
		// If true, record the reference into the database
		ScriptsTraverser scriptSearcher = new ScriptsTraverser(scriptsDir, testCasesSubpath)
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
		allTestObjectIds.forEach { testObjectId ->
			
			Set<ForwardReference> value = db.getAll(testObjectId)
			if (value == null) {
				// Oh, this TestObject must be a garbage
				// as no Test Case uses this
				tmp.add(testObjectId)
			}
		}
		List<TestObjectId> result = new ArrayList<>()
		result.addAll(tmp)
		Collections.sort(result)
		return result
	}

	String jsonifyGarbages() {
		List<TestObjectId> garbages = getGarbages()
		StringBuilder sb = new StringBuilder()
		sb.append("{")
		sb.append(JsonOutput.toJson("Project name"))
		sb.append(":")
		sb.append(JsonOutput.toJson(projectDir.getFileName().toString()))
		sb.append(",")
		sb.append(JsonOutput.toJson("objectRepositorySubpath"))
		sb.append(":")
		sb.append(JsonOutput.toJson(objectRepositorySubpaths))
		sb.append(",")
		sb.append(JsonOutput.toJson("testCasesSubpath"))
		sb.append(":")
		sb.append(JsonOutput.toJson(testCasesSubpaths))
		sb.append(",")
		sb.append(JsonOutput.toJson("stats"))
		sb.append(":")
		sb.append(JsonOutput.toJson(stats()))
		sb.append(",")
		sb.append(JsonOutput.toJson("Number of unused TestObjects"))
		sb.append(":")
		sb.append(JsonOutput.toJson(garbages.size()))
		sb.append(",")
		sb.append(JsonOutput.toJson("Unused TestObjects"))
		sb.append(":")
		sb.append("[")
		String sep = ""
		garbages.forEach { toi ->
			sb.append(sep)
			sb.append(toi.toJson())
			sep = ","
		}
		sb.append("]")
		sb.append("}")
		return JsonOutput.prettyPrint(sb.toString())
	}

	private Map<String, Number> stats() {
		Map<String, Number> stats = new LinkedHashMap<String, Number>()
		stats.put("Number of TestCases", numberOfTestCases)
		stats.put("Number of TestObjects", numberOfTestObjects)
		Duration timeTaken = Duration.between(startedAt, finishedAt)
		stats.put("Duration seconds", toSeconds(timeTaken))
		return stats
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

		private Path   objectRepositoryDir // non null
		private Path   testCasesDir // non null

		private List<String> testCasesSubpaths // could be empty

		Builder() {
			Path projectDir = Paths.get(RunConfiguration.getProjectDir()).toAbsolutePath().normalize()
			Path objrepoDir = projectDir.resolve("Object Repository")
			Path scriptsDir = projectDir.resolve("Scripts")
			init(objrepoDir, scriptsDir)
		}
		
		private void init(Path objrepoDir, Path scriptsDir) {
			Objects.requireNonNull(objrepoDir)
			Objects.requireNonNull(scriptsDir)
			assert Files.exists(objrepoDir)
			assert Files.exists(scriptsDir)
			this.objectRepositoryDir = objrepoDir
			this.testCasesDir = scriptsDir
			this.testCasesSubpaths = new ArrayList<>()
			this.testCasesSubpaths.add("")
		}
		
		Builder(Path objrepoDir, Path scriptsDir) {
			init(objrepoDir, scriptsDir)
		}

		Builder(File objrepoDir, File scriptsDir) {
			init(objrepoDir.toPath(), scriptsDir.toPath())
		}

		Builder testCasesSubpath(String... subpaths) {
			Objects.requireNonNull(subpaths)
			if (this.testCasesSubpaths.contains("")) {
				this.testCasesSubpaths.remove("")
			}
			(subpaths as List).forEach { subpath ->
				Path p = testCasesDir.resolve(subpath)
				assert Files.exists(p): "${p} does not exist"
				this.testCasesSubpaths.add(subpath)
			}
			return this
		}

		ObjectRepositoryGarbageCollector build() {
			return new ObjectRepositoryGarbageCollector(this)
		}
	}
}
