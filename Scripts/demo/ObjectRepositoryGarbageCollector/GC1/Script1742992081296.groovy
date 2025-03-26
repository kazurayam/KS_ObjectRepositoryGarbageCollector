import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.testobject.gc.ObjectRepositoryGarbageCollector
import com.kms.katalon.core.configuration.RunConfiguration

import demo.Reporter

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

// write it into a file
Reporter rp = new Reporter("ObjectRepositoryGarbageCollector/GC1.md")
rp.report("## Output of TestCases/demo/ObjectRepositoryGarbageCollector/GC1\n",
	"gc.garbages() returned\n",
	"```",
	json,
	"```")
