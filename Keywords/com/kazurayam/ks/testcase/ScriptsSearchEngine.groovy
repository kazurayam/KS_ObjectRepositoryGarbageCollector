package com.kazurayam.ks.testcase

import java.nio.file.Files
import java.nio.file.Path

public class ScriptsSearchEngine {

	private Path scriptsDir
	private TestCaseScriptsVisitor visitor

	ScriptsSearchEngine(Path scriptsDir) {
		Objects.requireNonNull(scriptsDir)
		assert Files.exists(scriptsDir)
		this.scriptsDir = scriptsDir
		this.visitor = init(scriptsDir)
	}

	private TestCaseScriptsVisitor init(Path scriptsDir) throws IOException {
		TestCaseScriptsVisitor vis = new TestCaseScriptsVisitor(scriptsDir)
		Files.walkFileTree(scriptsDir, vis)
		return vis
	}

	/**
	 * 
	 */
	TextSearchResultsCollection searchText(String pattern, Boolean isRegex) {
		Objects.requireNonNull(pattern)
		TextSearchResultsCollection collection = new TextSearchResultsCollection()
		List<Path> groovyFiles = visitor.getGroovyFiles()
		groovyFiles.forEach { p ->
			TestCaseSource source = new TestCaseSource(p)
			List<TextSearchResult> searchResults = source.searchText(pattern, isRegex)
			collection.put(p.toString(), searchResults)
		}
		return collection
	}

	/**
	 *
	 */
	TextSearchResultsCollection searchReferenceToTestObject(String testObjectId) {
		return this.searchText(testObjectId, false)
	}
}
