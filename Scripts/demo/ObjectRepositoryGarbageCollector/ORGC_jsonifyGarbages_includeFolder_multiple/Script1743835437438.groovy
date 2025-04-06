import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.testobject.gc.Garbages
import com.kazurayam.ks.testobject.gc.ObjectRepositoryGarbageCollector
import com.kms.katalon.core.configuration.RunConfiguration

import groovy.json.JsonOutput

/**
 * Similar to the GC script but
 * the TestObjects under the specified sub-folder in the "Object Repository" are selected.
 */
ObjectRepositoryGarbageCollector gc =
		new ObjectRepositoryGarbageCollector.Builder()
			.includeFolder("main/Page_CURA Healthcare Service")
			.includeFolder("main/Page_CURA Healthcare Service/xtra")
			.build()

String json = gc.jsonifyGarbages()

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path classOutputDir = projectDir.resolve("build/tmp/testOutput/demo/ObjectRepositoryGarbageCollector")
Path outDir = classOutputDir.resolve("includeFolder_single")
Files.createDirectories(outDir)
File outFile = outDir.resolve("garbages.json").toFile()

outFile.text = JsonOutput.prettyPrint(json)

Garbages garbages = gc.getGarbages()
assert 4 == garbages.size() : "expected garbages.size()==4 but was ${garbages.size()}"

