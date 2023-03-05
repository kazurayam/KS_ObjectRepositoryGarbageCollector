package com.kazurayam.ks.gc

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * A sort of "Garbage Collector" for the "Object Repository" of Katalon Studio.
 * It reports list of Test Objects with metadata which Test Case scripts use the Test Object. 
 * This class can report a list of "unused" Test Objects, which I call "garbages".
 * 
 * This class just compiles a report. It does not actually remove or change Test Objects at all.
 * 
 * @author kazurayam
 */
class ObjectRepositoryGC {

	private static final projectDir = Paths.get(".")

	private Path   objrepoDir // non null
	private Path   scriptsDir // non null
	private String objrepoSubpath // can be null
	private String scriptsSubpath // can be null

	private ObjectRepositoryGC(Builder builder) {
		this.objrepoDir = builder.objrepoDir
		this.scriptsDir = builder.scriptsDir
		this.objrepoSubpath = builder.objrepoSubpath
		this.scriptsSubpath = builder.scriptsSubpath
	}

	/**
	 * 
	 */
	void dryrun() {
		throw new RuntimeException("TODO")
	}




	/**
	 * Joshua Bloch's Builder pattern in Effective Java
	 * 
	 * @author kazuarayam
	 */
	public static class Builder {

		private Path   objrepoDir // non null
		private Path   scriptsDir // non null

		private String objrepoSubpath // can be null
		private String scriptsSubpath // can be null

		Builder(Path objrepoDir, Path scriptsDir) {
			Objects.requireNonNull(objrepoDir)
			assert Files.exists(objrepoDir)
			Objects.requireNonNull(scriptsDir)
			assert Files.exists(scriptsDir)
			this.objrepoDir = objrepoDir
			this.scriptsDir = scriptsDir
		}

		Builder objrepoSubpath(String subpath) {
			Objects.requireNonNull(subpath)
			Path p = objrepoDir.resolve(subpath)
			assert Files.exists(p)
			this.objrepoSubpath = subpath
			return this
		}

		Builder scriptsSubpath(String subpath) {
			Objects.requireNonNull(subpath)
			Path p = scriptsDir.resolve(subpath)
			assert Files.exists(p)
			this.scriptsSubpath = subpath
			return this
		}

		ObjectRepositoryGC build() {
			return new ObjectRepositoryGC(this)
		}

	}
	
}
