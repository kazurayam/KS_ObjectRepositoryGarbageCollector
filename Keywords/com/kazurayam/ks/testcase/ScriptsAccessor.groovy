package com.kazurayam.ks.testcase

import java.nio.file.Files
import java.nio.file.Path

import com.kazurayam.ant.DirectoryScanner

public class ScriptsAccessor {

	private Path scriptsDir
	private DirectoryScanner ds
	private List<Path> groovyFiles

	private ScriptsAccessor(Builder builder) {
		scriptsDir = builder.scriptsDir
		ds = builder.ds
	}

	public List<Path> getGroovyFiles() {
		String[] includes = ['**/*.groovy']
		ds.setIncludes(includes)
		ds.scan()
		String[] includedFiles = ds.getIncludedFiles()
		groovyFiles = new ArrayList<>()
		for (int i = 0; i < includedFiles.length; i++) {
			groovyFiles.add(scriptsDir.resolve(includedFiles[i])
					.toAbsolutePath().normalize())
		}
		return this.groovyFiles
	}
	
	
	
	/**
	 * 
	 * @author kazurayam
	 */
	public static class Builder {
		
		private Path scriptsDir
		private DirectoryScanner ds
		
		Builder(Path scriptsDir) {
			Objects.requireNonNull(scriptsDir)
			assert Files.exists(scriptsDir)
			this.scriptsDir = scriptsDir.toAbsolutePath().normalize()
			ds = new DirectoryScanner()
			ds.setBasedir(scriptsDir.toFile())
		}
		
		ScriptsAccessor build() {
			return new ScriptsAccessor(this)
		}
	}
}
