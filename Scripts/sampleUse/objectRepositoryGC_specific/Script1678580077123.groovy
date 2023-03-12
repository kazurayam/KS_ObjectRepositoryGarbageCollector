import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.gc.ObjectRepositoryGC
import com.kms.katalon.core.configuration.RunConfiguration

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path buildDir = projectDir.resolve("build")
Files.createDirectories(buildDir)
Path report = buildDir.resolve("garbages2.json")

Path objrepoDir = projectDir.resolve("Object Repository")
Path scriptsDir = projectDir.resolve("Scripts")

ObjectRepositoryGC gc = new ObjectRepositoryGC.Builder(objrepoDir, scriptsDir)
								.objrepoSubpath("Page_CURA Healthcare Service")
								.scriptsSubpath("main")
								.build()

String json = gc.garbages()
report.text = json
