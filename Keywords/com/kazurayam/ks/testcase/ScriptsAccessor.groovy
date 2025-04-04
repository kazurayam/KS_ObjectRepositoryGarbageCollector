package com.kazurayam.ks.testcase

import com.kazurayam.ant.DirectoryScanner

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
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
		//initAlt()
	}

	private void init() {
		DirectoryScanner ds = new DirectoryScanner()
		ds.setBasedir(scriptsDir.toFile())
		String[] includes = ['**/*.groovy']
		ds.setIncludes(includes)
		ds.scan()
		String[] includedFiles = ds.getIncludedFiles()
		groovyFiles = new ArrayList<>()
		for (int i = 0; i < includedFiles.length; i++) {
			
			groovyFiles.add(scriptsDir.resolve(includedFiles[i]).toAbsolutePath().normalize())
		}
	}

	public List<Path> getGroovyFiles() {
		return this.groovyFiles
	}


	private void initAlt() {
		ScriptsVisitor scriptsVisitor = new ScriptsVisitor(scriptsDir)
		Files.walkFileTree(scriptsDir, scriptsVisitor)
		groovyFiles = scriptsVisitor.getGroovyFiles()
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
