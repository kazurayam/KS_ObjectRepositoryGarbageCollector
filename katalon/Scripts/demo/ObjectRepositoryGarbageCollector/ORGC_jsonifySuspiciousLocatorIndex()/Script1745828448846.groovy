import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.logging.SimplifiedStopWatch
import com.kazurayam.ks.testobject.combine.ObjectRepositoryGarbageCollector
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import groovy.json.JsonOutput

/**
 * ObjectRepositoryGarbageCollector#jsonifySuspiciousLocatorIndex() demonstration
 */

ObjectRepositoryGarbageCollector gc =
	new ObjectRepositoryGarbageCollector.Builder()
		.includeObjectRepositoryFolder("**/Page_CURA*")
		.build()
		
SimplifiedStopWatch ssw = new SimplifiedStopWatch()

String json = gc.jsonifySuspiciousLocatorIndex()

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path classOutputDir = projectDir.resolve("build/tmp/testOutput/demo/ObjectRepositoryGarbageCollector")
Path outDir = classOutputDir.resolve("ORGC_jsonifySuspiciousLocatorIndex")
Files.createDirectories(outDir)
File outFile = outDir.resolve("suspicious.json").toFile()

outFile.text = JsonOutput.prettyPrint(json)

ssw.stop()
WebUI.comment(ssw.toString())
