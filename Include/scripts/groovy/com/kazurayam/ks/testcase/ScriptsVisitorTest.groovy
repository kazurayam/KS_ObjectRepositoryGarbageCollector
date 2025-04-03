package com.kazurayam.ks.testcase

import static org.junit.Assert.*

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import groovy.json.JsonOutput
import com.kazurayam.ks.reporting.Shorthand

import internal.GlobalVariable


@RunWith(JUnit4.class)
public class ScriptsVisitorTest {

	private static final Path scriptsDir = Paths.get("./Scripts")
	private static final String subpath = "main"
	private ScriptsVisitor visitor

	@Test
	void test_getGroovyFiles() throws IOException {
		visitor = new ScriptsVisitor(scriptsDir)
		Files.walkFileTree(scriptsDir.resolve(subpath), visitor)
		List<Path> list = visitor.getGroovyFiles()
		assertTrue(list.size() > 0) 
		list.forEach { p ->
			assertTrue("`${p.toString()}` is expected to contain a string 'main'", p.toString().contains("main"))
		}
		//
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
						.fileName("test_getGroovyFiles.json").build()
		sh.write(JsonOutput.prettyPrint(toJson(list)))
	}

	private String toJson(List<Path> it) {
		StringBuilder sb = new StringBuilder()
		String sep = ""
		sb.append("[")
		it.forEach( { p ->
			sb.append(sep)
			sb.append("\"")
			sb.append(p.toString())
			sb.append("\"")
			sep = ", "
		})
		sb.append("]")
		return sb.toString()
	}
}
