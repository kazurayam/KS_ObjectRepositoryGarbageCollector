package com.kazurayam.ks.testobject.gc

import static org.junit.Assert.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.FixMethodOrder;
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters;

import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testcase.DigestedLine
import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testobject.Locator
import com.kazurayam.ks.testobject.TestObjectEssence
import com.kazurayam.ks.testobject.TestObjectId

import groovy.json.JsonOutput

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)
public class ForwardReferenceTest {

	private ForwardReference fref

	@BeforeClass
	static void beforeClass() {}

	/*
	 * prepare an instance of ForwardReference as text fixture
	 */
	@Before
	void setup() {
		fref = createSampleInstance()
	}

	public static final ForwardReference createSampleInstance() {
		String testCaseIdValue = "main/TC1"
		String line = '''WebUI.click(findTestObject('Object Repository/main/Page_CURA Healthcare Service/a_Make Appointment')'''
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
		assertEquals("main/TC1", fref.getTestCaseId().getValue())
	}

	@Test
	void test_digestedLine() {
		assertEquals('''WebUI.click(findTestObject('Object Repository/main/Page_CURA Healthcare Service/a_Make Appointment')''',
				fref.getDigestedLine().getLine())
	}

	@Test
	void test_testObjectEssence() {
		TestObjectEssence essence = fref.getTestObjectEssence()
		assertEquals("Page_CURA Healthcare Service/a_Make Appointment", essence.getTestObjectId().getValue())
		assertEquals("""//a[@id='btn-make-appointment']""", essence.getLocator().getValue())
	}

	@Test
	void test_toJson() {
		String json = fref.toJson()
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_toJson.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue(json.contains("Page_CURA Healthcare Service/a_Make Appointment"))
	}

	@Test
	void test_toString() {
		String s = fref.toString()
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_toString.json").build()
		sh.write(JsonOutput.prettyPrint(s))
		assertTrue(fref.toJson().contains("Page_CURA Healthcare Service/a_Make Appointment"))
	}
}
