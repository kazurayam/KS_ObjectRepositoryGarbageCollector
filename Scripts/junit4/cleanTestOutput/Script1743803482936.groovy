import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.apache.commons.io.FileUtils

import com.kms.katalon.core.configuration.RunConfiguration

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path outputDir = projectDir.resolve("build/tmp/testOutput/junit4")
FileUtils.deleteDirectory(outputDir.toFile())
Files.createDirectories(outputDir)

