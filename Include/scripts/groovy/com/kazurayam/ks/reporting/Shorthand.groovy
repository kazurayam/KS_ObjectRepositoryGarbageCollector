package com.kazurayam.ks.reporting
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kms.katalon.core.configuration.RunConfiguration

class Shorthand {

	private Path filePath

	private Shorthand(Builder builder) {
		this.filePath = builder.filePath
	}

	Path write(String... content) {
		OutputStream os = filePath.newOutputStream()
		OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8)
		BufferedWriter bw = new BufferedWriter(osw)
		PrintWriter pw = new PrintWriter(bw)
		//
		content.each { it ->
			pw.println(it)
		}
		//
		pw.flush()
		pw.close()
		os.close()
		return filePath
	}

	/**
	 * 
	 */
	public static class Builder {
		private Path baseDir = Paths.get(RunConfiguration.getProjectDir()).resolve("build")
						.resolve("tmp").resolve("testOutput").normalize().toAbsolutePath()
		private String subDir = null
		private String fileName = "out.txt"
		private Path filePath

		Builder() {}
		Builder(Path baseDir) {
			Objects.requireNonNull(baseDir)
			if (!Files.exists(baseDir)) {
				throw new IllegalArgumentException(baseDir.toString() + " is not present")
			}
			this.baseDir = baseDir
		}
		Builder subDir(String p) {
			Objects.requireNonNull(p)
			this.subDir = p
			return this
		}
		Builder fileName(String f) {
			Objects.requireNonNull(f)
			this.fileName = f
			return this
		}
		Shorthand build() {
			filePath = baseDir.resolve(subDir).resolve(fileName)
			Path parentDir = filePath.getParent()
			if (!Files.exists(parentDir)) {
				Files.createDirectories(parentDir)
			}
			return new Shorthand(this)
		}
	}
}
