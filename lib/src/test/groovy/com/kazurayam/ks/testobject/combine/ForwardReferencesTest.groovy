package com.kazurayam.ks.testobject.combine

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testobject.TestObjectId

import groovy.json.JsonOutput

@RunWith(JUnit4.class)
class ForwardReferencesTest {

	private ForwardReferences forwardReferences
	private TestCaseId testCaseId
	private ForwardReference reference
	private TestObjectId testObjectId

	@Before
	void setup() {
		forwardReferences = new ForwardReferences()
		testCaseId = new TestCaseId("main/TC1")
		reference = ForwardReferenceTest.createSampleInstance()
		forwardReferences.add(reference)
		//
		assertEquals("main/TC1", reference.getTestCaseId().getValue())
		testObjectId = reference.getTestObjectId()
		assertEquals("Page_CURA Healthcare Service/a_Make Appointment",
				testObjectId.getValue())
	}

	@Test
	void test_size() {
		assertEquals(1, forwardReferences.size())
	}

	@Test
	void test_get() {
		ForwardReference ref = forwardReferences.get(0)
		assertNotNull(ref)
	}

	@Test
	void test_getAll() {
		Set<ForwardReference> allRefs = forwardReferences.getAll()
		assertEquals(1, allRefs.size())
	}

	@Test
	void test_toString() {
		String s = forwardReferences.toString()
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_toString.json").build()
		sh.write(JsonOutput.prettyPrint(s))
		assertTrue(s.contains("main/TC1"))
		assertTrue(s.contains("Page_CURA Healthcare Service/a_Make Appointment"))
	}

	@Test
	void test_toJson() {
		String json = forwardReferences.toJson()
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_toJson.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue(json.contains("main/TC1"))
		assertTrue(json.contains("Page_CURA Healthcare Service/a_Make Appointment"))
	}

	@Test
	void test_findForwardReferencesFrom() {
		Set<ForwardReference> filled = forwardReferences.findForwardReferencesFrom(new TestCaseId("main/TC1"))
		assertEquals(1, filled.size())
		//
		Set<ForwardReference> empty = forwardReferences.findForwardReferencesFrom(new TestCaseId("foo"))
		assertEquals(0, empty.size())
	}

	@Test
	void test_containsTestCaseId() {
		assertTrue(forwardReferences.containsTestCaseId(new TestCaseId("main/TC1")))
		assertFalse(forwardReferences.containsTestCaseId(new TestCaseId("foo")))
	}

	@Test
	void test_getAllTestCaseIdsContaind() {
		Set<TestCaseId> testCaseIds = forwardReferences.getAllTestCaseIdsContained()
		assertEquals(1, testCaseIds.size())
	}

	@Test
	void test_findForwardReferencesTo() {
		Set<ForwardReference> filled = forwardReferences.findForwardReferencesTo(new TestObjectId("Page_CURA Healthcare Service/a_Make Appointment"))
		assertEquals(1, filled.size())
		//
		Set<ForwardReference> empty = forwardReferences.findForwardReferencesTo(new TestObjectId("bar"))
		assertEquals(0, empty.size())
	}

	@Test
	void test_containsTestObjectId() {
		assertTrue(forwardReferences.containsTestObjectId(new TestObjectId("Page_CURA Healthcare Service/a_Make Appointment")))
		assertFalse(forwardReferences.containsTestObjectId(new TestObjectId("bar")))
	}

	@Test
	void test_getAllTestObjectIdsContained() {
		Set<TestObjectId> testObjectIds = forwardReferences.getAllTestObjectIdsContained()
		assertEquals(1, testObjectIds.size())
	}
}
