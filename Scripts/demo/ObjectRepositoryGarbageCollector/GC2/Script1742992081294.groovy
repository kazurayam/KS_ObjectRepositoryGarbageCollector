import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.testobject.gc.ObjectRepositoryGarbageCollector
import com.kms.katalon.core.configuration.RunConfiguration

import demo.Reporter

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path objrepoDir = projectDir.resolve("Object Repository")
Path scriptsDir = projectDir.resolve("Scripts")

ObjectRepositoryGarbageCollector gc = new ObjectRepositoryGarbageCollector.Builder(objrepoDir, scriptsDir)
								.objrepoSubpath("Page_CURA Healthcare Service")
								.scriptsSubpath("main")
								.build()

String json = gc.garbages()
								
// write it into a file
Reporter rp = new Reporter("ObjectRepositoryGarbageCollector/GC2.md")
rp.report("## Output of Test Cases/demo/ObjectRepositoryGarbageCollector/GC2\n",
	"gc.garbages() returned\n",
	"```", 
	json, 
	"```")
								