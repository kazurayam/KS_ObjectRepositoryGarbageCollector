package com.kazurayam.ks.testobject

import java.nio.file.Files
import java.nio.file.Path

import com.kazurayam.ant.DirectoryScanner

public class ObjectRepositoryAccessor {

	private Path objectRepositoryDir
	private DirectoryScanner ds
	
	public ObjectRepositoryAccessor(Path orDir) {
		Objects.requireNonNull(orDir)
		assert Files.exists(orDir)
		this.objectRepositoryDir = orDir.toAbsolutePath().normalize()
		init()
	}

	private void init() {
		ds = new DirectoryScanner()
		ds.setBasedir(objectRepositoryDir.toFile())
	}
	
	public String[] getIncludedFiles() {
		String[] includes = ['**/*.rs']
		ds.setIncludes(includes)
		ds.scan()
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
}
