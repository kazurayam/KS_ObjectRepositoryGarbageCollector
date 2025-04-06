package com.kazurayam.ks.testobject

import java.nio.file.Files
import java.nio.file.Path
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.kazurayam.ant.DirectoryScanner

public class ObjectRepositoryAccessor {

	private static Logger logger = LoggerFactory.getLogger(ObjectRepositoryAccessor.class)
	
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
			if (includedFiles[i].endsWith(".rs")) {
				TestObjectId toi = new TestObjectId(includedFiles[i].replaceAll('\\.rs$', ''))
				result.add(toi)
			} else {
				logger.warn("found a file that does not end with '.rs'; ${includedFiles[i]}")
			}
		}
		return result
	}

	public List<Path> getRsFiles() {
		String[] includedFiles = getIncludedFiles()
		List<Path> result = new ArrayList<>()
		for (int i = 0; i < includedFiles.length; i++) {
			if (includedFiles[i].endsWith(".rs")) {
				Path rs = objectRepositoryDir.resolve(includedFiles[i])
							.toAbsolutePath().normalize()
				result.add(rs)
			} else {
				logger.warn("found a file that does not end with '.rs'; ${includedFiles[i]}")
			}
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
