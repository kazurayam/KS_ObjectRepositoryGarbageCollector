import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.testobject.combine.ObjectRepositoryGarbageCollector
import com.kms.katalon.core.configuration.RunConfiguration

import groovy.json.JsonOutput

/**
 * ObjectRepositoryGarbageCollector#jsonifySuspiciousLocatorIndex() demonstration
 */

ObjectRepositoryGarbageCollector gc =
	new ObjectRepositoryGarbageCollector.Builder()
		.includeObjectRepositoryFolder("**/Page_CURA*")
		.build()

String json = gc.jsonifySuspiciousLocatorIndex()

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path classOutputDir = projectDir.resolve("build/tmp/testOutput/demo/ObjectRepositoryGarbageCollector")
Path outDir = classOutputDir.resolve("ORGC_jsonifySuspiciousLocatorIndex")
Files.createDirectories(outDir)
File outFile = outDir.resolve("suspicious.json").toFile()

outFile.text = JsonOutput.prettyPrint(json)
