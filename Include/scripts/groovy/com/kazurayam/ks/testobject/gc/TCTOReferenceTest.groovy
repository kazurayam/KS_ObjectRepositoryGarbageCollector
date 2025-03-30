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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)
public class TCTOReferenceTest {

	private TCTOReference instance
	private TestCaseId testCaseId
	private DigestedLine textSearchResult
	private TestObjectEssence testObjectEssence

	@BeforeClass
	static void beforeClass() {}

	/*
	 * convenient method to create an instance of TCTOReference as text fixture
	 */
	@Before
	void setup() {
		instance = createSampleInstance()
	}

	public static final TCTOReference createSampleInstance() {
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
		return new TCTOReference(testCaseId, textSearchResult, testObjectEssence)
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
		println "********** test_toJson **********"
		println json
		assertTrue(json.contains("Page_CURA Healthcare Service/a_Make Appointment"))
		//
		//JsonNode input = objectMapper.readTree(instance.toJson())
		//Expression<JsonNode> expression = jmespath.compile("TCTOReference.TestObjectEssence.TestObjectId")
		//JsonNode result = expression.search(input)
		//assertEquals("\"Page_CURA Healthcare Service/a_Make Appointment\"", result.toString())
		//
		//expression = jmespath.compile("TCTOReference.TestObjectEssence.Locator")
		//result = expression.search(input)
		//assertEquals("\"//a[@id='btn-make-appointment']\"", result.toString())
	}

	@Test
	void test_toString() {
		println "********** test_toString **********"
		println instance.toString()
		assertTrue(instance.toJson().contains("Page_CURA Healthcare Service/a_Make Appointment"))
	}
}
