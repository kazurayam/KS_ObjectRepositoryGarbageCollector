package com.kazurayam.ks.testobject.combine

import java.nio.file.Path
import java.nio.file.Paths

import static org.junit.Assert.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.FixMethodOrder;
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters;

import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.text.DigestedLine
import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testobject.TestObjectId

import groovy.json.JsonOutput

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)
class ForwardReferenceTest {

	private ForwardReference forwardReference

	@BeforeClass
	static void beforeClass() {}

	/*
	 * prepare an instance of ForwardReference as text fixture
	 */
	@Before
	void setup() {
		forwardReference = createSampleInstance()
	}

	static final ForwardReference createSampleInstance() {
		String testCaseIdValue = "main/TC1"
		String line = '''WebUI.click(findTestObject('Object Repository/main/Page_CURA Healthcare Service/a_Make Appointment')'''
		TestCaseId testCaseId = new TestCaseId(testCaseIdValue)
		DigestedLine textSearchResult = new DigestedLine.Builder(line, 9)
				.pattern('Page_CURA Healthcare Service/a_Make Appointment', false)
				.build()
		Path relativePath = Paths.get("main/Page_CURA Healthcare Service/a_Make Appointment.rs")
		TestObjectId testObjectId = new TestObjectId(relativePath)
		return new ForwardReference(testCaseId, textSearchResult, testObjectId)
	}

	@Test
	void test_testCaseId() {
		assertEquals("main/TC1", forwardReference.getTestCaseId().getValue())
	}

	@Test
	void test_digestedLine() {
		assertEquals('''WebUI.click(findTestObject('Object Repository/main/Page_CURA Healthcare Service/a_Make Appointment')''',
				forwardReference.getDigestedLine().getLine())
	}

	@Test
	void test_getTestObjectId() {
		TestObjectId toi = forwardReference.getTestObjectId()
		assertEquals("main/Page_CURA Healthcare Service/a_Make Appointment", toi.getValue())
	}

	@Test
	void test_toJson() {
		String json = forwardReference.toJson()
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_toJson.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue(json.contains("Page_CURA Healthcare Service/a_Make Appointment"))
	}

	@Test
	void test_toString() {
		String s = forwardReference.toString()
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_toString.json").build()
		sh.write(JsonOutput.prettyPrint(s))
		assertTrue(forwardReference.toJson().contains("Page_CURA Healthcare Service/a_Make Appointment"))
	}
}
