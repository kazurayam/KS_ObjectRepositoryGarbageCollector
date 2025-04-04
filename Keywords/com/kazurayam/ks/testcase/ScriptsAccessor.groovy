package com.kazurayam.ks.testcase

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

public class ScriptsAccessor {

	private Path scriptsDir
	private List<Path> groovyFiles

	public ScriptsAccessor(Path scriptsDir) {
		Objects.requireNonNull(scriptsDir)
		assert Files.exists(scriptsDir)
		this.scriptsDir = scriptsDir.toAbsolutePath().normalize()
		init()
	}

	private void init() {
		ScriptsVisitor scriptsVisitor = new ScriptsVisitor(scriptsDir)
		Files.walkFileTree(scriptsDir, scriptsVisitor)
		groovyFiles = scriptsVisitor.getGroovyFiles()
	}

	public List<Path> getGroovyFiles() {
		return this.groovyFiles
	}
	
	
	
	/**
 	 * This class visits the <projectDir>/Scripts directory recursively
	 * to make a list of Paths of *.groovy files, which is Katalon's Test Case scripts
	 *
	 * @author kazurayam
	 */
	public static class ScriptsVisitor extends SimpleFileVisitor<Path> {

		private Path scriptsDir
		private List<Path> groovyFiles

		ScriptsVisitor(Path scriptsDir) {
			Objects.requireNonNull(scriptsDir)
			assert Files.exists(scriptsDir)
			this.scriptsDir = scriptsDir.toAbsolutePath().normalize()
			groovyFiles = new ArrayList<Path>()
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
			if ( !Files.isDirectory(file) && file.getFileName().toString().endsWith(".groovy")) {
				groovyFiles.add(file)
			}
			return FileVisitResult.CONTINUE;
		}

		List<Path> getGroovyFiles() {
			List<Path> result = new ArrayList()
			groovyFiles.forEach { p ->
				result.add(p)
			}
			Collections.sort(result)
			return result
		}
	}

}
