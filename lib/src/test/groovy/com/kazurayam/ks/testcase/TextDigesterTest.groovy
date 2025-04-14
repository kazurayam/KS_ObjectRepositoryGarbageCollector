package com.kazurayam.ks.testcase

import com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver
import com.kazurayam.ks.reporting.Shorthand
import groovy.json.JsonOutput
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.nio.file.Path
import java.nio.file.Paths

import static org.junit.Assert.assertEquals

@RunWith(JUnit4.class)
public class TextDigesterTest {

	private Path TC1 = KatalonProjectDirectoryResolver.getProjectDir()
			.resolve("Scripts/main/TC1/Script1677544889443.groovy")

	@Test
	void test_digestText() {
		TextDigester digester = new TextDigester(TC1)
		String pattern = "Page_CURA Healthcare Service/a_Make Appointment"
		List<DigestedLine> list = digester.digestText(pattern, false)
		assertEquals(1, list.size())
		StringBuilder sb = new StringBuilder()
		sb.append("[")
		String sep = ""
		list.forEach { tsr ->
			sb.append(sep)
			sb.append(tsr.toJson())
			sep = ","
		}
		sb.append("]")
		//
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_digestText.json").build()
		String json = JsonOutput.prettyPrint(sb.toString())
		sh.write(json)
	}
}
