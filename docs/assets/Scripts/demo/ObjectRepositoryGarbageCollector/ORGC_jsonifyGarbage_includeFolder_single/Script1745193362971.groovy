import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.testobject.combine.Garbage
import com.kazurayam.ks.testobject.combine.ObjectRepositoryGarbageCollector
import com.kms.katalon.core.configuration.RunConfiguration

import groovy.json.JsonOutput

/**
 * Similar to the GC script but 
 * the TestObjects under the specified sub-folder in the "Object Repository" are selected. 
 */
ObjectRepositoryGarbageCollector gc = 
		new ObjectRepositoryGarbageCollector.Builder()
			.includeObjectRepositoryFolder("main/Page_CURA Healthcare Service")
			.build()

String json = gc.jsonifyGarbage()

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path classOutputDir = projectDir.resolve("build/tmp/testOutput/demo/ObjectRepositoryGarbageCollector")
Path outDir = classOutputDir.resolve("ORGC_jsonifyGarbage_includeFolder_single")
Files.createDirectories(outDir)
File outFile = outDir.resolve("garbage.json").toFile()

outFile.text = JsonOutput.prettyPrint(json)

Garbage garbage = gc.getGarbage()
assert 4 == garbage.size() : "expected garbage.size()==4 but was ${garbage.size()}"

