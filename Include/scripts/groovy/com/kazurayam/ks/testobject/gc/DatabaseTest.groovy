package com.kazurayam.ks.testobject.gc

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testobject.TestObjectId

import groovy.json.JsonOutput
import internal.GlobalVariable

@RunWith(JUnit4.class)
public class DatabaseTest {

	private Database db
	private TestCaseId testCaseId
	private ForwardReference reference
	private TestObjectId testObjectId

	@Before
	void setup() {
		db = new Database()
		testCaseId = new TestCaseId("main/TC1")
		reference = ForwardReferenceTest.createSampleInstance()
		db.add(reference)
		//
		assertEquals("main/TC1", reference.getTestCaseId().value())
		testObjectId = reference.getTestObjectEssence().testObjectId()
		assertEquals("Page_CURA Healthcare Service/a_Make Appointment",
				testObjectId.value())
	}

	@Test
	void test_size() {
		assertEquals(1, db.size())
	}

	@Test
	void test_get() {
		ForwardReference ref = db.get(0)
		assertNotNull(ref)
	}

	@Test
	void test_getAll() {
		Set<ForwardReference> allRefs = db.getAll()
		assertEquals(1, allRefs.size())
	}

	@Test
	void test_toString() {
		String s = db.toString()
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName("test_toString.json").build()
		sh.write(JsonOutput.prettyPrint(s))
		assertTrue(s.contains("main/TC1"))
		assertTrue(s.contains("Page_CURA Healthcare Service/a_Make Appointment"))
	}

	@Test
	void test_toJson() {
		String json = db.toJson()
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName("test_toJson.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue(json.contains("main/TC1"))
		assertTrue(json.contains("Page_CURA Healthcare Service/a_Make Appointment"))
	}

	@Test
	void test_findForwardReferencesFrom() {
		Set<ForwardReference> filled = db.findForwardReferencesFrom(new TestCaseId("main/TC1"))
		assertEquals(1, filled.size())
		//
		Set<ForwardReference> empty = db.findForwardReferencesFrom(new TestCaseId("foo"))
		assertEquals(0, empty.size())
	}

	@Test
	void test_containsTestCaseId() {
		assertTrue(db.containsTestCaseId(new TestCaseId("main/TC1")))
		assertFalse(db.containsTestCaseId(new TestCaseId("foo")))
	}

	@Test
	void test_getAllTestCaseIdsContaind() {
		Set<TestCaseId> testCaseIds = db.getAllTestCaseIdsContained()
		assertEquals(1, testCaseIds.size())
	}

	@Test
	void test_findForwardReferencesTo() {
		Set<ForwardReference> filled = db.findForwardReferencesTo(new TestObjectId("Page_CURA Healthcare Service/a_Make Appointment"))
		assertEquals(1, filled.size())
		//
		Set<ForwardReference> empty = db.findForwardReferencesTo(new TestObjectId("bar"))
		assertEquals(0, empty.size())
	}

	@Test
	void test_containsTestObjectId() {
		assertTrue(db.containsTestObjectId(new TestObjectId("Page_CURA Healthcare Service/a_Make Appointment")))
		assertFalse(db.containsTestObjectId(new TestObjectId("bar")))
	}

	@Test
	void test_getAllTestObjectIdsContained() {
		Set<TestObjectId> testObjectIds = db.getAllTestObjectIdsContained()
		assertEquals(1, testObjectIds.size())
	}
}
