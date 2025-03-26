import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.testobject.gc.ObjectRepositoryGarbageCollector
import com.kms.katalon.core.configuration.RunConfiguration

Path projectDir = Paths.get(RunConfiguration.getProjectDir())


Path objrepoDir = projectDir.resolve("Object Repository")
Path scriptsDir = projectDir.resolve("Scripts")

ObjectRepositoryGarbageCollector gc = new ObjectRepositoryGarbageCollector.Builder(objrepoDir, scriptsDir)
								.objrepoSubpath("Page_CURA Healthcare Service")
								.scriptsSubpath("main")
								.build()

Path buildDir = projectDir.resolve("build")
Files.createDirectories(buildDir)
Path report = buildDir.resolve("debmoB.json")
report.text = gc.garbages()
