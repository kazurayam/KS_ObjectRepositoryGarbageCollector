package com.kazurayam.ks.testobject

import com.kazurayam.ant.DirectoryScanner
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.Path

class ObjectRepositoryAccessor {

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
		if (includeFilesSpecification.size() > 0) {
			String[] includes = includeFilesSpecification.toArray(new String[0])
			ds.setIncludes(includes)
		}
		ds.scan()
	}

	String[] getIncludedFiles() {
		return ds.getIncludedFiles()
	}

	List<TestObjectId> getTestObjectIdList() {
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

	List<Path> getRsFiles() {
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
	static class Builder {
		private Path objectRepositoryDir
		private List<String> includeFiles
		Builder(Path orDir) {
			objectRepositoryDir = orDir.toAbsolutePath().normalize()
			includeFiles = new ArrayList<>()
		}
		Builder includeFile(String pattern) {
			Objects.requireNonNull(pattern)
			includeFiles.add(pattern)
			return this
		}
		Builder includeFiles(List<String> patterns) {
			Objects.requireNonNull(patterns)
			includeFiles.addAll(patterns)
			return this
		}
		ObjectRepositoryAccessor build() {
			return new ObjectRepositoryAccessor(this)
		}
	}
}
