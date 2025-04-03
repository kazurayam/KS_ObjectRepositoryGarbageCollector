package com.kazurayam.ks.testcase

import static org.junit.Assert.*

import java.nio.file.Path
import java.nio.file.Paths

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kazurayam.ks.reporting.Shorthand

import groovy.json.JsonOutput
import internal.GlobalVariable

@RunWith(JUnit4.class)
public class ScriptDigesterTest {

	private Path scriptsDir
	private ScriptDigester traverser

	@Before
	void setup() {
		scriptsDir = Paths.get("./Scripts").toAbsolutePath()
		traverser = new ScriptDigester(scriptsDir, "main")
	}


	@Test
	void test_digestTestCase() {
		TestCaseId testCaseId = new TestCaseId("main/TC1")
		String pattern = "a_Make Appointment"
		Boolean isRegex = false
		List<DigestedLine> result = traverser.digestTestCase(testCaseId, pattern, isRegex)
		assertNotNull(result)
		assertEquals(1, result.size())
		//
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_searchIn.json").build()
		String json = JsonOutput.prettyPrint(JsonOutput.toJson(result.get(0).toJson()))
		sh.write(json)
	}
}