package com.kazurayam.ks.reporting

import com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * A utility that creates a directory and write a file into the directory.
 * The directory will be in the format of
 * "<libProjectDir>/build/tmp/testOutput/<subDir>/<fileName>"
 *
 * It is recommended to specify "subDir" to be the fully-qualified-class-name of
 * each individual unit-test, e.g.,
 *
 * `build/tmp/testOutput/com.kazurayam.ks.testobject.TestObjectEssense/test_toJson.json`
 *
 * `Shorthand` class is designed to be developed by the unit-tests.
 * `Shorthand` class is helpful to shorten the code of unit-tests, as it hides
 * cumbersome codes of preparing output directory and file to store the test result.
 */
class Shorthand {

	private Path filePath

	private Shorthand(Builder builder) {
		this.filePath = builder.filePath
	}

	Path write(List<String> content) {
		String[] strings = content.toArray(new String[0])
		this.write(strings)
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
	static class Builder {
		private Path baseDir = Paths.get(".").resolve("build")
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
