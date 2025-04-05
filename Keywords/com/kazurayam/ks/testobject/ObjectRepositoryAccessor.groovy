package com.kazurayam.ks.testobject

import java.nio.file.Files
import java.nio.file.Path

import com.kazurayam.ant.DirectoryScanner

public class ObjectRepositoryAccessor {

	private Path objectRepositoryDir
	private List<String> includeFilesSpecification
	private DirectoryScanner ds

	private ObjectRepositoryAccessor(Builder builder) {
		this.objectRepositoryDir = builder.objectRepositoryDir
		this.includeFilesSpecification = builder.includeFiles
		init()
	}

	private void init() {
		ds = new DirectoryScanner()
		ds.setBasedir(objectRepositoryDir.toFile())
		includeFilesSpecification.each { pattern ->
			if (pattern.length() > 0) {
				ds.setIncludes(pattern)
			}
		}
		ds.scan()
	}

	public String[] getIncludedFiles() {
		return ds.getIncludedFiles()
	}

	public List<TestObjectId> getTestObjectIdList() {
		String[] includedFiles = getIncludedFiles()
		List<TestObjectId> result = new ArrayList<>()
		for (int i = 0; i < includedFiles.length; i++) {
			TestObjectId toi = new TestObjectId(includedFiles[i].replaceAll('\\.rs$', ''))
			result.add(toi)
		}
		return result
	}

	public List<Path> getRsFiles() {
		List<String> includedFiles = getIncludedFiles()
		List<Path> result = new ArrayList<>()
		includedFiles.each { f ->
			result.add(objectRepositoryDir.resolve(f)
					.toAbsolutePath().normalize())
		}
		return result
	}


	/**
	 * 
	 * @author kazurayam
	 */
	public static class Builder {
		private Path objectRepositoryDir
		private List<String> includeFiles
		public Builder(Path orDir) {
			this.objectRepositoryDir = orDir.toAbsolutePath().normalize()
			this.includeFiles = new ArrayList<>()
		}
		public Builder includeFiles(List<String> patterns) {
			Objects.requireNonNull(patterns)
			this.includeFiles = patterns
			return this
		}
		public ObjectRepositoryAccessor build() {
			return new ObjectRepositoryAccessor(this)
		}
	}
}
