package com.kazurayam.ks.testcase

import java.nio.file.Files
import java.nio.file.Path

import com.kazurayam.ant.DirectoryScanner

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

}
