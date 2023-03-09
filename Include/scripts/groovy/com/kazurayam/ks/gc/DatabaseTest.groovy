package com.kazurayam.ks.gc

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kazurayam.ks.gc.Database
import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testcase.TextSearchResult
import com.kazurayam.ks.testcase.TextSearchResult.Builder
import com.kazurayam.ks.testobject.TestObjectId
import com.kazurayam.ks.testobject.TestObjectGist

import groovy.json.JsonOutput

@RunWith(JUnit4.class)
public class DatabaseTest {

	private Database db
	private TestCaseId testCaseId
	private TCTOReference reference
	private TestObjectId testObjectId

	@Before
	void setup() {
		db = new Database()
		testCaseId = new TestCaseId("main/TC1")
		reference = TCTOReferenceTest.createSampleInstance()
		db.add(reference)
		//
		assertEquals("main/TC1", reference.testCaseId().value())
		testObjectId = reference.testObjectGist().testObjectId()
		assertEquals("Page_CURA Healthcare Service/a_Make Appointment",
				testObjectId.value())
	}

	@Test
	void test_size() {
		assertEquals(1, db.size())
	}

	@Test
	void test_get() {
		TCTOReference ref = db.get(0)
		assertNotNull(ref)
	}

	@Test
	void test_getAll() {
		Set<TCTOReference> allRefs = db.getAll()
		assertEquals(1, allRefs.size())
	}

	@Test
	void test_toString() {
		String s = db.toString()
		println "********** test_toString **********"
		println db.toString()
		assertTrue(s.contains("main/TC1"))
		assertTrue(s.contains("Page_CURA Healthcare Service/a_Make Appointment"))
	}

	@Test
	void test_toJson() {
		println "*********** test_toJson **********"
		println JsonOutput.prettyPrint(db.toJson(true))
	}

	@Test
	void test_findTCTOReferencesOf_TestCaseId() {
		Set<TCTOReference> filled = db.findTCTOReferencesOf(new TestCaseId("main/TC1"))
		assertEquals(1, filled.size())
		//
		Set<TCTOReference> empty = db.findTCTOReferencesOf(new TestCaseId("foo"))
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
	void test_findTCTOReferencesOf_TestObjectId() {
		Set<TCTOReference> filled = db.findTCTOReferencesOf(new TestObjectId("Page_CURA Healthcare Service/a_Make Appointment"))
		assertEquals(1, filled.size())
		//
		Set<TCTOReference> empty = db.findTCTOReferencesOf(new TestObjectId("bar"))
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
