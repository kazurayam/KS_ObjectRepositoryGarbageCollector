package com.kazurayam.ks.testobject

import java.nio.file.Files
import java.nio.file.Path

import com.kazurayam.ant.DirectoryScanner

public class ObjectRepositoryAccessor {

	private Path objectRepositoryDir
	private DirectoryScanner ds
	private List<Path> rsFiles
	
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
	
	public List<Path> getRsFiles() {
		String[] includes = ['**/*.rs']
		ds.setIncludes(includes)
		ds.scan()
		String[] includedFiles = ds.getIncludedFiles()
		rsFiles = new ArrayList<>()
		for (int i = 0; i < includedFiles.length; i++) {
			rsFiles.add(objectRepositoryDir.resolve(includedFiles[i])
				.toAbsolutePath().normalize())
		}
		return this.rsFiles
	}
}
