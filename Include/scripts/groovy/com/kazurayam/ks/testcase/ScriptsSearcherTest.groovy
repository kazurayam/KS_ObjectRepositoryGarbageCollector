package com.kazurayam.ks.testcase

import static org.junit.Assert.*

import java.nio.file.Path
import java.nio.file.Paths

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import groovy.json.JsonOutput

@RunWith(JUnit4.class)
public class ScriptsSearcherTest {

	private Path scriptsDir
	private ScriptsSearcher searcher

	@Before
	void setup() {
		scriptsDir = Paths.get("./Scripts").toAbsolutePath()
		searcher = new ScriptsSearcher(scriptsDir, "main")
	}

	@Test
	void test_searchText() {
		String pattern = "https://katalon-demo-cura.herokuapp.com/"
		Map<TestCaseId, List<TextSearchResult>> result =
				searcher.searchText(pattern, false)
		assertEquals(2, result.size())
	}

	@Test
	void test_searchReferenceToTestObject() {
		String testObjectId = "Page_CURA Healthcare Service/a_Make Appointment"
		Map<TestCaseId, List<TextSearchResult>> result =
				searcher.searchReferenceToTestObject(testObjectId)
		assertEquals(2, result.size())
		println "********** searchReferenceToTestObject **********"
		println JsonOutput.prettyPrint(JsonOutput.toJson(result))
		TestCaseId testCaseId1 = result.keySet().getAt(0)   // main/New Test Case/Script1677842574205.groovy
		TestCaseId testCaseId2 = result.keySet().getAt(1)   // main/TC1/Script1677544889443.groovy
		assertTrue("'${testCaseId1}' should start with 'main'",
			testCaseId1.value().startsWith("main"))
		assertTrue("'${testCaseId2}' should have value with 'line'",
			result.get(testCaseId2).get(0).toJson().contains("line"))
	}
	
	@Test
	void test_searchIn() {
		TestCaseId testCaseId = new TestCaseId("main/TC1")
		String pattern = "a_Make Appointment"
		Boolean isRegex = false
		List<TextSearchResult> result = searcher.searchIn(testCaseId, pattern, isRegex)
		assertNotNull(result)
		assertEquals(1, result.size())
		println "********** test_searchIn **********"
		println JsonOutput.prettyPrint(result.get(0).toJson())
	}
}