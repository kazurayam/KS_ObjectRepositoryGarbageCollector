package com.kazurayam.ks.testcase

import java.nio.file.Files
import java.nio.file.Path

import com.kazurayam.ks.gc.Database

public class ScriptsSearcher {

	private Path scriptsDir
	private Path targetDir
	private TestCaseScriptsVisitor visitor

	ScriptsSearcher(Path scriptsDir) {
		this(scriptsDir, null)
	}

	ScriptsSearcher(Path scriptsDir, String subDir) {
		Objects.requireNonNull(scriptsDir)
		assert Files.exists(scriptsDir)
		this.scriptsDir = scriptsDir.toAbsolutePath()
		if (subDir == null) {
			this.targetDir = scriptsDir
		} else {
			this.targetDir = scriptsDir.resolve(subDir)
		}
		this.visitor = init(targetDir)
	}

	private TestCaseScriptsVisitor init(Path scriptsDir) throws IOException {
		TestCaseScriptsVisitor vis = new TestCaseScriptsVisitor(scriptsDir)
		Files.walkFileTree(targetDir, vis)
		return vis
	}

	List<TextSearchResult> searchIn(TestCaseId testCaseId, String pattern, Boolean isRegex) {
		Objects.requireNonNull(testCaseId)
		Objects.requireNonNull(pattern)
		Objects.requireNonNull(isRegex)
		Path groovyFile = scriptsDir.resolve(testCaseId.value() + ".rs")
		SearchableText source = new SearchableText(file)
		List<TextSearchResult> searchResults = source.searchText(pattern, isRegex)
		return searchResults
	}

	/**
	 * 
	 */
	Map<TestCaseId, List<TextSearchResult>> searchText(String pattern, Boolean isRegex) {
		Objects.requireNonNull(pattern)
		Objects.requireNonNull(isRegex)
		Map<TestCaseId, List<TextSearchResult>> result = new TreeMap<>()
		List<Path> groovyFiles = visitor.getGroovyFiles()
		groovyFiles.forEach { p ->
			try {
				Path file = targetDir.resolve(p)
				SearchableText source = new SearchableText(file)
				List<TextSearchResult> searchResults = source.searchText(pattern, isRegex)
				TestCaseId id = new TestCaseId(p.toString())
				result.put(id, searchResults)
			} catch (IOException e) {
				throw new RuntimeException(e)
			}
		}
		return result
	}

	/**
	 *
	 */
	Map<TestCaseId, List<TextSearchResult>> searchReferenceToTestObject(String testObjectId) {
		return this.searchText(testObjectId, false)
	}
}
