package com.kazurayam.ks.testobject.gc

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static org.junit.Assert.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.FixMethodOrder;
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters;

import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testcase.DigestedLine
import com.kazurayam.ks.testobject.Locator
import com.kazurayam.ks.testobject.TestObjectEssence
import com.kazurayam.ks.testobject.TestObjectId
import com.kazurayam.ks.reporting.Shorthand

import groovy.json.JsonOutput
import internal.GlobalVariable
import com.kazurayam.ks.testobject.TestObjectId

import groovy.json.JsonOutput
import internal.GlobalVariable

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)
public class ForwardReferenceTest {

	private ForwardReference instance
	private TestCaseId testCaseId
	private DigestedLine textSearchResult
	private TestObjectEssence testObjectEssence

	@BeforeClass
	static void beforeClass() {}

	/*
	 * prepare an instance of ForwardReference as text fixture
	 */
	@Before
	void setup() {
		instance = createSampleInstance()
	}

	public static final ForwardReference createSampleInstance() {
		String testCaseIdValue = "main/TC1"
		String line = '''WebUI.click(findTestObject('Object Repository/Page_CURA Healthcare Service/a_Make Appointment')'''
		String togValue = "Page_CURA Healthcare Service/a_Make Appointment"
		String locValue = """//a[@id='btn-make-appointment']"""
		TestCaseId testCaseId = new TestCaseId(testCaseIdValue)
		DigestedLine textSearchResult = new DigestedLine.Builder(line, 9)
				.pattern(locValue, false)
				.build()
		TestObjectEssence testObjectEssence = new TestObjectEssence(
				new TestObjectId(togValue),
				"BASIC",
				new Locator(locValue))
		return new ForwardReference(testCaseId, textSearchResult, testObjectEssence)
	}

	@Test
	void test_testCaseId() {
		assertEquals("main/TC1", instance.testCaseId().value())
	}

	@Test
	void test_textSearchResult() {
		assertEquals('''WebUI.click(findTestObject('Object Repository/Page_CURA Healthcare Service/a_Make Appointment')''',
				instance.textSearchResult().line())
	}

	@Test
	void test_testObjectEssence() {
		TestObjectEssence essence = instance.testObjectEssence()
		assertEquals("Page_CURA Healthcare Service/a_Make Appointment", essence.testObjectId().value())
		assertEquals("""//a[@id='btn-make-appointment']""", essence.locator().value())
	}

	@Test
	void test_toJson() {
		String json = instance.toJson()
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName("test_toJson.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue(json.contains("Page_CURA Healthcare Service/a_Make Appointment"))
	}

	@Test
	void test_toString() {
		String s = instance.toString()
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName("test_toString.json").build()
		sh.write(JsonOutput.prettyPrint(s))
		assertTrue(instance.toJson().contains("Page_CURA Healthcare Service/a_Make Appointment"))
	}
}
