package com.kazurayam.ks.testobject

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

	private Path objectRepositoryDir
	private Path scriptsDir

	ObjectRepositoryGC() {
		this(projectDir.resolve("Object Repository"), projectDir.resolve("Scripts"))
	}

	ObjectRepositoryGC(Path objectRepositoryDir, Path scriptsDir) {
		this.objectRepositoryDir = objectRepositoryDir
		this.scriptsDir = scriptsDir
	}

	void dryrun() {
		throw new RuntimeException("TODO")
	}
}
