import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.testobject.gc.ObjectRepositoryGarbageCollector
import com.kms.katalon.core.configuration.RunConfiguration

/**
 * outputs a JSON file which contains a list of garbage Test Objects
 * in the Object Repository directory.
 * A garbage Test Object is a Test Object which is unused by any of Test Cases.
 */

// the Garbage Collector instance will scan the Object Repository directory
// and the Scripts directory
ObjectRepositoryGarbageCollector gc = new ObjectRepositoryGarbageCollector.Builder().build()

// the gc.garbages() method call can compile a list of garbate Test Objects,
// output the information in a JSON string
String json = gc.garbages()
println json

// write it into a file
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path buildDir = projectDir.resolve("build")
Files.createDirectories(buildDir)
Path report = buildDir.resolve("demoA.json")
report.text = json
