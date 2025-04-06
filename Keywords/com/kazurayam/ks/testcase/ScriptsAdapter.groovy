package com.kazurayam.ks.testcase

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class ScriptsAdapter {

	private static Logger logger = LoggerFactory.getLogger(ScriptsAdapter.class)

	private Path scriptsDir
	private List<String> includeFoldersSpecification
	private ScriptsAccessor accessor

	private ScriptsAdapter(Builder builder) {
		scriptsDir = builder.scriptsDir
		includeFoldersSpecification = builder.includeFolders
		Objects.requireNonNull(scriptsDir)
		Objects.requireNonNull(includeFoldersSpecification)
		init()
	}

	private init() {
		// the following line is the whole reason why we need this class "ScriptsAdapter"
		List<String> patterns = translatePatterns(includeFoldersSpecification)
		accessor = new ScriptsAccessor.Builder(scriptsDir)
				.includeFiles(patterns).build()
	}

	/**
	 * convert a patten for Scripts sub-folders to a pattern for Groovy files
	 * E.g, "** /ObjectRepositoryGarbageCollector" -> 
	 * 			"** /ObjectRepositoryGarbageCollector/** /*.groovy"
	 */
	protected List<String> translatePatterns(List<String> patternsForFolder) {
		List<String> patternsForFile = new ArrayList()
		patternsForFolder.each { ptrn ->
			StringBuilder sb = new StringBuilder()
			sb.append(ptrn)
			if (!ptrn.endsWith("/")) {
				sb.append("/")
			}
			sb.append("**/*.groovy")
			patternsForFile.add(sb.toString())
		}
		return patternsForFile
	}

	Path getScriptsDir() {
		return scriptsDir
	}

	List<String> getIncludeFoldersSpecification() {
		return includeFoldersSpecification
	}

	public List<Path> getGroovyFiles() {
		return accessor.getGroovyFiles()
	}


	public static class Builder {
		private Path scriptsDir
		private List<String> includeFolders
		public Builder() {
			this(Paths.get(".").resolve("Scripts"))
		}
		public Builder(Path dir) {
			Objects.requireNonNull(dir)
			assert Files.exists(dir)
			scriptsDir = dir
			includeFolders = new ArrayList<>()
		}
		public Builder includeFolder(String pattern) {
			includeFolders.add(pattern)
			return this
		}
		public Builder includeFolders(List<String> includeFolders) {
			includeFolders.addAll(includeFolders)
			return this
		}
		public ScriptsAdapter build() {
			assert scriptsDir != null : "scriptsDir is left null"
			return new ScriptsAdapter(this)
		}
	}
}
