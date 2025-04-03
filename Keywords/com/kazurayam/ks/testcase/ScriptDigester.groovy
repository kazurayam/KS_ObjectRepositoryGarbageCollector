package com.kazurayam.ks.testcase

import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

public class ScriptDigester {

	private Path scriptsDir
	private ScriptsVisitor visitor

	ScriptDigester(Path scriptsDir) {
		this(scriptsDir, null)
	}

	ScriptDigester(Path scriptsDir, String subPath) {
		Objects.requireNonNull(scriptsDir)
		assert Files.exists(scriptsDir)
		this.scriptsDir = scriptsDir.toAbsolutePath().normalize()  // calling .normalize() is significant
		this.visitor = init(scriptsDir, subPath)
	}

	private ScriptsVisitor init(Path scriptsDir, subPath) throws IOException {
		ScriptsVisitor visitor = new ScriptsVisitor(scriptsDir)
		Path targetDir
		if (subPath == null) {
			targetDir = scriptsDir
		} else {
			targetDir = scriptsDir.resolve(subPath)
		}
		Files.walkFileTree(targetDir, visitor)
		return visitor
	}

	List<DigestedLine> digestTestCase(TestCaseId testCaseId, String pattern, Boolean isRegex) {
		Objects.requireNonNull(testCaseId)
		Objects.requireNonNull(pattern)
		Objects.requireNonNull(isRegex)
		Path testCaseDir = scriptsDir.resolve(testCaseId.value)
		Path groovyFile = findChildGroovyFile(testCaseDir)
		SearchableText source = new SearchableText(groovyFile)
		List<DigestedLine> searchResults = source.searchText(pattern, isRegex)
		return searchResults
	}

	private Path findChildGroovyFile(Path testCaseDir) {
		if (Files.isDirectory(testCaseDir)) {
			List<Path> fileList = Files.list(testCaseDir)
					.filter({ p -> !Files.isDirectory(p) })
					.filter({ p -> p.getFileName().toString().endsWith(".groovy")})
					.collect(Collectors.toList())
			if (fileList.size() == 0) {
				throw new IOException("${testCaseDir.toString()} contains no *.groovy file; should not happen")
			} else if (fileList.size() == 1) {
				return fileList.get(0)
			} else {
				throw new IOException("${testCaseDir.toString()} contains 2 or more *.groovy files; should not happen")
			}
		} else {
			throw new IOException("${testCaseDir.toString()} is not a directory")
		}
	}
}
