import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.combine.ObjectRepositoryGarbageCollector
import com.kms.katalon.core.configuration.RunConfiguration

import groovy.json.JsonOutput
import internal.GlobalVariable

/**
 * ObjectRepositoryGarbageCollector#jsonifyCombinedLocatorIndex() demonstration
 */

ObjectRepositoryGarbageCollector gc =
	new ObjectRepositoryGarbageCollector.Builder()
		.includeObjectRepositoryFolder("**/Page_CURA*")
		.build()

String json = gc.jsonifyCombinedLocatorIndex()

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path classOutputDir = projectDir.resolve("build/tmp/testOutput/demo/ObjectRepositoryGarbageCollector")
Path outDir = classOutputDir.resolve("ORGC_jsonifyCombinedLocatorIndex")
Files.createDirectories(outDir)
File outFile = outDir.resolve("garbage.json").toFile()

outFile.text = JsonOutput.prettyPrint(json)
