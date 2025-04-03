import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.testobject.gc.ObjectRepositoryGarbageCollector
import com.kms.katalon.core.configuration.RunConfiguration

/**
 * Similar to the GC script but 
 * This scans the selected sub-path(s) under the "Object Repository". 
 * This scans the selected sub-path(s) under the "Test Cases".
 * This writes the result into a file rather than the console.
 */
ObjectRepositoryGarbageCollector gc = 
		new ObjectRepositoryGarbageCollector.Builder()
			.objectRepositorySubpaths("Page_CURA Healthcare Service")
			.scriptsSubpaths(["demo", "main"])
			.build()
String json = gc.garbages()

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path outDir = projectDir.resolve("build/tmp/testOutput/demo/ObjectRepositoryGarbageCollector/GC_selective")
Files.createDirectories(outDir)
File outFile = outDir.resolve("garbages.json").toFile()

outFile.text = json
