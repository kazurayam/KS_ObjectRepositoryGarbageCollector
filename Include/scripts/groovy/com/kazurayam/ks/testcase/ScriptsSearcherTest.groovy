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
		println JsonOutput.prettyPrint(JsonOutput.toJson(result))
	}
}