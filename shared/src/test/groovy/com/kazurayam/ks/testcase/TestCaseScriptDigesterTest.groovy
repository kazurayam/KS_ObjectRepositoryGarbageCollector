package com.kazurayam.ks.testcase

import com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver
import com.kazurayam.ks.reporting.Shorthand
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.nio.file.Path
import java.nio.file.Paths

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

@RunWith(JUnit4.class)
public class TestCaseScriptDigesterTest {

	private Path scriptsDir
	private TestCaseScriptDigester digester

	@Before
	void setup() {
		scriptsDir = KatalonProjectDirectoryResolver.getProjectDir()
				.resolve("Scripts").toAbsolutePath()
		digester = new TestCaseScriptDigester(scriptsDir)
	}


	@Test
	void test_digestTestCase() {
		TestCaseId testCaseId = new TestCaseId("main/TC1")
		String pattern = "a_Make Appointment"
		Boolean isRegex = false
		List<DigestedLine> result = digester.digestTestCase(testCaseId, pattern, isRegex)
		assertNotNull(result)
		assertEquals(1, result.size())
		//
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_searchIn.json").build()
		String json = JsonOutput.prettyPrint(JsonOutput.toJson(result.get(0).toJson()))
		sh.write(json)
	}
}