package com.kazurayam.ks.gc

import static org.junit.Assert.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.FixMethodOrder;
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters;
import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testcase.TextSearchResult
import com.kazurayam.ks.testobject.TestObjectGist
import com.kazurayam.ks.testobject.TestObjectId
import com.kazurayam.ks.testobject.Locator

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)
public class TCTOReferenceTest {
	
	private TCTOReference instance
	private TestCaseId testCaseId
	private TextSearchResult textSearchResult
	private TestObjectGist testObjectGist
	
	@BeforeClass
	static void beforeClass() {}
	
	@Before
	void setup() {
		String testCaseIdValue = "main/TC1"
		String line = '''WebUI.click(findTestObject('Object Repository/Page_CURA Healthcare Service/a_Make Appointment')'''
		String togValue = "Page_CURA Healthcare Service/a_Make Appointment"
		String locValue = """//a[@id='btn-make-appointment']"""
		testCaseId = new TestCaseId(testCaseIdValue)
		textSearchResult = new TextSearchResult.Builder(line, 9)
								.pattern(locValue, false)
								.build()
		testObjectGist = new TestObjectGist(
								new TestObjectId(togValue),
								"BASIC",
								new Locator(locValue))
		instance = new TCTOReference(testCaseId, textSearchResult, testObjectGist)
	}
	
	@Test
	void test_testCaseId() {
		assertEquals(testCaseId, instance.testCaseId())
	}
	
	@Test
	void test_textSearchResult() {
		assertEquals(textSearchResult, instance.textSearchResult())
		
	}
	
	@Test
	void test_testObjectGist() {
		assertEquals(testObjectGist, instance.testObjectGist())
	}
}
