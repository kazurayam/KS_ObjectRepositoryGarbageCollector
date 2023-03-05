package com.kazurayam.ks.gc

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import com.kazurayam.ks.gc.TCTOReference
import com.kazurayam.ks.testcase.ScriptsSearcher
import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testcase.TestCaseScriptsVisitor
import com.kazurayam.ks.testcase.TextSearchResult
import com.kazurayam.ks.testobject.ExtendedObjectRepository
import com.kazurayam.ks.testobject.TestObjectGist
import com.kazurayam.ks.testobject.TestObjectId
import groovy.json.JsonOutput


/**
 * A sort of "Garbage Collector" for the "Object Repository" of Katalon Studio.
 * It reports list of Test Objects with metadata which Test Case scripts use the Test Object. 
 * This class can report a list of "unused" Test Objects, which I call "garbages".
 * 
 * This class just compiles a report. It does not actually remove or change Test Objects at all.
 * 
 * @author kazurayam
 */
class ObjectRepositoryGC {

	private static final projectDir = Paths.get(".")

	private Path   objrepoDir // non null
	private Path   scriptsDir // non null
	private String objrepoSubpath // can be null
	private String scriptsSubpath // can be null

	private Database db;

	private ObjectRepositoryGC(Builder builder) {
		this.objrepoDir = builder.objrepoDir
		this.scriptsDir = builder.scriptsDir
		this.objrepoSubpath = builder.objrepoSubpath
		this.scriptsSubpath = builder.scriptsSubpath
		this.db = new Database()
	}

	/*
	 * This method will scan the "Object Repository" folder and the "Scripts" folder.
	 * to create an instance of Database internally and fill it with information found 
	 * out of the directories.
	 * You can retrieve the Database by calling "db()" method.
	 * You can retrieve an Garbage Collection plan by calling "xref()" method, in which you can
	 * find a list of "garbage" Test Objects which are not used by any of the Test Cases.
	 */
	public void scan() {
		// scan the Object Repository directory to make a list of TestObjectGists
		ExtendedObjectRepository extOR = new ExtendedObjectRepository(objrepoDir, objrepoSubpath)
		List<TestObjectGist> gistList = extOR.listGistRaw("", false)

		// scan the Scripts directory to make a list of TestCaseIds
		Path sdir = (scriptsSubpath != null) ? scriptsDir.resolve(scriptsSubpath) : scriptsSubpath
		TestCaseScriptsVisitor tcsVisitor = new TestCaseScriptsVisitor(scriptsDir)
		Files.walkFileTree(sdir, tcsVisitor)
		List<TestCaseId> testCaseIdList = tcsVisitor.getTestCaseIdList()

		// Iterate over the list of TestCaseIds.
		// Read the TestCase script, check if it contains any references to the TestObjects.
		// If true, record the reference into the database
		ScriptsSearcher scriptSearcher = new ScriptsSearcher(scriptsDir, scriptsSubpath)
		testCaseIdList.forEach { testCaseId ->
			gistList.forEach { gist ->
				TestObjectId testObjectId = gist.id()
				List<TextSearchResult> textSearchResultList =
						scriptSearcher.searchIn(testCaseId, testObjectId.value(), false)
				textSearchResultList.forEach { textSearchResult ->
					TCTOReference reference = new TCTOReference(testCaseId, textSearchResult, gist)
					db.put(testCaseId, reference)
				}
			}
		}
	}

	Database db() {
		return db
	}


	Map<TestObjectId, Set<TCTOReference>> resolveRaw() {
		Map<TestObjectId, Set<TCTOReference>> result = new TreeMap<>()
		Set<TCTOReference> allReferences = db.getAll()
		allReferences.forEach { ref ->
			TestObjectId testObjectId = ref.testObjectGist().id()
			result.put(testObjectId, ref)
		}
		return result
	}

	/**
	 *
	 */
	String resolve() {
		Map<TestObjectId, Set<TCTOReference>> resolved = this.resolveRaw()
		String json = JsonOutput.toJson(resolved)
		String pp = JsonOutput.prettyPrint(json)
		return pp
	}

	/**
	 * 
	 */
	List<TestObjectId> garbagesRaw() {
		Set<TestObjectId> tmp = new TreeSet<>()
		Map<TestObjectId, Set<TCTOReference>> resolved = this.resolveRaw()
		resolved.keySet().forEach { testObjectId ->
			Set<TCTOReference> value = resolved.get(testObjectId)
			if (value.size() == 0) {
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

	String garbages() {
		List<TestObjectId> garbages = garbagesRaw()
		String json = JsonOutput.toJson(garbages)
		String pp = JsonOutput.prettyPrint(json)
		return pp
	}

	/**
	 * Joshua Bloch's Builder pattern in Effective Java
	 * 
	 * @author kazuarayam
	 */
	public static class Builder {

		private Path   objrepoDir // non null
		private Path   scriptsDir // non null

		private String objrepoSubpath // can be null
		private String scriptsSubpath // can be null

		Builder(Path objrepoDir, Path scriptsDir) {
			Objects.requireNonNull(objrepoDir)
			assert Files.exists(objrepoDir)
			Objects.requireNonNull(scriptsDir)
			assert Files.exists(scriptsDir)
			this.objrepoDir = objrepoDir
			this.scriptsDir = scriptsDir
		}

		Builder objrepoSubpath(String subpath) {
			Objects.requireNonNull(subpath)
			Path p = objrepoDir.resolve(subpath)
			assert Files.exists(p)
			this.objrepoSubpath = subpath
			return this
		}

		Builder scriptsSubpath(String subpath) {
			Objects.requireNonNull(subpath)
			Path p = scriptsDir.resolve(subpath)
			assert Files.exists(p)
			this.scriptsSubpath = subpath
			return this
		}

		ObjectRepositoryGC build() {
			return new ObjectRepositoryGC(this)
		}

	}

}
