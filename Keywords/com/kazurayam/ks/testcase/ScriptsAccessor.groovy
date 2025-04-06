package com.kazurayam.ks.testcase

import java.nio.file.Files
import java.nio.file.Path
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.kazurayam.ant.DirectoryScanner

public class ScriptsAccessor {

	private static Logger logger = LoggerFactory.getLogger(ScriptsAccessor.class)

	private Path scriptsDir
	private List<String> includeFilesSpecification
	private DirectoryScanner ds

	private ScriptsAccessor(Builder builder) {
		scriptsDir = builder.scriptsDir
		includeFilesSpecification = builder.includeFiles
		init()
	}

	private void init() {
		ds = new DirectoryScanner()
		ds.setBasedir(scriptsDir.toFile())
		if (includeFilesSpecification.size() > 0) {
			String[] includes = includeFilesSpecification.toArray(new String[0])
			ds.setIncludes(includes)
		}
		ds.scan()
	}

	public String[] includedFiles() {
		return ds.getIncludedFiles()
	}

	public List<Path> getGroovyFiles() {
		String[] includedFiles = ds.getIncludedFiles()
		List<Path> groovyFiles = new ArrayList<>()
		for (int i = 0; i < includedFiles.length; i++) {
			if (includedFiles[i].endsWith(".groovy")) {
				Path p = scriptsDir.resolve(includedFiles[i])
						.toAbsolutePath().normalize()
				groovyFiles.add(p)
			} else {
				logger.warnEnabled("found a file that does not end with '.groovy'; ${includedFiles[i]}")
			}
		}
		return groovyFiles
	}



	/**
	 * 
	 * @author kazurayam
	 */
	public static class Builder {
		private Path scriptsDir
		private List<String> includeFiles
		Builder(Path scriptsDir) {
			Objects.requireNonNull(scriptsDir)
			assert Files.exists(scriptsDir)
			this.scriptsDir = scriptsDir.toAbsolutePath().normalize()
			includeFiles = new ArrayList<>()
		}
		public Builder includeFile(String pattern) {
			Objects.requireNonNull(pattern)
			includeFiles.add(pattern)
			return this
		}
		public Builder includeFiles(List<String> patterns) {
			Objects.requireNonNull(patterns)
			includeFiles.addAll(patterns)
			return this
		}
		ScriptsAccessor build() {
			return new ScriptsAccessor(this)
		}
	}
}
