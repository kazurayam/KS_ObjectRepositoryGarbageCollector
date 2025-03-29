package com.kazurayam.ks.reporting
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kms.katalon.core.configuration.RunConfiguration


class Shorthand {

	private Path filePath
	private TargetDocumentFormat docFormat
	private String syntaxHighlighting

	private Shorthand(Builder builder) {
		this.filePath = builder.filePath
		this.docFormat = builder.docFormat
		this.syntaxHighlighting = builder.syntaxHighlighting
	}

	void write(String... content) {
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
	}

	void code(String... code) {
		OutputStream os = filePath.newOutputStream()
		OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8)
		BufferedWriter bw = new BufferedWriter(osw)
		PrintWriter pw = new PrintWriter(bw)
		//
		if (docFormat == TargetDocumentFormat.ASCIIDOC) {
			pw.println("[source,${syntaxHighlighting}]")
			pw.println("----")
		} else {
			pw.println("```")
		}
		// print the lines of the code
		code.each { it ->
			pw.println(it)
		}
		if (docFormat == TargetDocumentFormat.ASCIIDOC) {
			pw.println("----")
		} else {
			pw.println("````")
		}
		//
		pw.flush()
		pw.close()
		os.close()
	}

	void codeWithHeader(String header, String... code) {
		Objects.requireNonNull(header)
		OutputStream os = filePath.newOutputStream()
		OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8)
		BufferedWriter bw = new BufferedWriter(osw)
		PrintWriter pw = new PrintWriter(bw)
		//
		pw.println(header)
		//
		if (docFormat == TargetDocumentFormat.ASCIIDOC) {
			pw.println("[source,${syntaxHighlighting}]")
			pw.println("----")
		} else {
			pw.println("```")
		}
		//
		code.each { it ->
			pw.println(it)
		}
		if (docFormat == TargetDocumentFormat.ASCIIDOC) {
			pw.println("----")
		} else {
			pw.println("````")
		}
		//
		pw.flush()
		pw.close()
		os.close()
	}

	/**
	 * 
	 */
	public static class Builder {
		private Path baseDir = Paths.get(RunConfiguration.getProjectDir())
		.resolve("build").resolve("reports")
		.normalize().toAbsolutePath()
		private String subDir = null
		private String fileName = "report.txt"
		private Path filePath
		private TargetDocumentFormat docFormat = TargetDocumentFormat.MARKDOWN
		private String syntaxHighlighting = "groovy"

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
		Builder docFormat(TargetDocumentFormat docFormat) {
			Objects.requireNonNull(docFormat)
			this.docFormat = docFormat
			return this
		}
		Builder syntaxHighlighting(String syntaxHighlighting) {
			Objects.requireNonNull(syntaxHighlighting)
			this.syntaxHighlighting = syntaxHighlighting
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
