package com.kazurayam.ks.testcase

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable
import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.nio.file.Path
import java.nio.file.Paths
import groovy.json.JsonOutput

@RunWith(JUnit4.class)
public class ScriptsSearchEngineTest {

	private Path scriptsDir
	private ScriptsSearchEngine engine

	@Before
	void setup() {
		scriptsDir = Paths.get("./Scripts").toAbsolutePath()
		engine = new ScriptsSearchEngine(scriptsDir, "main")
	}

	@Test
	void test_searchText() {
		String pattern = "https://katalon-demo-cura.herokuapp.com/"
		TextSearchResultsCollection result =
				engine.searchText(pattern, false)
		assertEquals(1, result.size())
	}

	@Test
	void test_searchReferenceToTestObject() {
		String testObjectId = "Page_CURA Healthcare Service/a_Make Appointment"
		TextSearchResultsCollection result =
				engine.searchReferenceToTestObject(testObjectId)
		assertEquals(1, result.size())
		println JsonOutput.prettyPrint(result.toJson())
	}
}