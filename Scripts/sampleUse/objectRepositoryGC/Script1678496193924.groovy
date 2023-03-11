import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.gc.ObjectRepositoryGC
import com.kms.katalon.core.configuration.RunConfiguration

/**
 * generates a JSON file which shows a list of garbage Test Objects
 * in the Object Repository directory.
 * Here I call Test Objects unused by Test Cases as "garbage".
 * You can specify a sub-directory in the "Object Repository" from which 
 * Test Objects are selected.
 * Also you can specify a sub-directory in the "Test Cases" from which
 * Test Case scripts are selected. 
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path buildDir = projectDir.resolve("build")
Files.createDirectories(buildDir)
Path report = buildDir.resolve("garbages.json")

Path objrepoDir = projectDir.resolve("Object Repository")
Path scriptsDir = projectDir.resolve("Scripts")

ObjectRepositoryGC gc = new ObjectRepositoryGC.Builder(objrepoDir, scriptsDir)
							.scriptsSubpath("main")
							.objrepoSubpath("Page_CURA Healthcare Service")
							.build()
String json = gc.garbages()
report.text = json
