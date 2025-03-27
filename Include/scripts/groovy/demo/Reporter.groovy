package demo
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kms.katalon.core.configuration.RunConfiguration


class Reporter {

	private Path outDir = Paths.get(RunConfiguration.getProjectDir()).resolve("build").resolve("reports").resolve("demo")
	private Path outFile

	Reporter() {
		this("report.txt")
	}

	Reporter(String filePath) {
		outFile = outDir.resolve(filePath)
		Path parent = outFile.getParent()
		if (!Files.exists(parent)) {
			Files.createDirectories(parent)
		}
	}

	void report(String... content) {
		OutputStream os = outFile.newOutputStream()
		OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8)
		BufferedWriter bw = new BufferedWriter(osw)
		PrintWriter pw = new PrintWriter(bw)
		content.each { it ->
			pw.println(it)
		}
		pw.flush()
		pw.close()
		os.close()
	}
}
