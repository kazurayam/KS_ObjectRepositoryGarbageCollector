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
public class ScriptsTraverserTest {

	private Path scriptsDir
	private ScriptsTraverser traverser

	@Before
	void setup() {
		scriptsDir = Paths.get("./Scripts").toAbsolutePath()
		traverser = new ScriptsTraverser(scriptsDir, "main")
	}

	@Test
	void test_searchText() {
		String pattern = "https://katalon-demo-cura.herokuapp.com/"
		Map<TestCaseId, List<DigestedLine>> result =
				traverser.searchText(pattern, false)
		assertEquals(2, result.size())
	}

	@Test
	void test_searchReferenceToTestObject() {
		String testObjectId = "Page_CURA Healthcare Service/a_Make Appointment"
		Map<TestCaseId, List<DigestedLine>> result =
				traverser.searchReferenceToTestObject(testObjectId)
		assertEquals(2, result.size())
		//
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_searchReferenceToTestObject.json").build()
		String json = JsonOutput.prettyPrint(JsonOutput.toJson(result))
		sh.write(json)
		//
		TestCaseId testCaseId1 = result.keySet().getAt(0)   // main/TC1/Script1677544889443.groovy
		assertTrue("'${testCaseId1}' should start with 'main'",
				testCaseId1.value().startsWith("main"))
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