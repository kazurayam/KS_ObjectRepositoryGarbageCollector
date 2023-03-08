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
		assertEquals("main/TC1", reference.testCaseId().value())
		testObjectId = reference.testObjectGist().id()
		assertEquals("Page_CURA Healthcare Service/a_Make Appointment", testObjectId.value())
		db.put(testCaseId, reference)
	}

	@Test
	void test_keySet_size() {
		assertEquals(1, db.keySet().size())
	}

	@Test
	void test_containsKey() {
		assertTrue(db.containsKey(testCaseId))
	}

	@Test
	void test_get() {
		Set<TCTOReference> set = db.get(testCaseId)
		assertEquals(1, set.size())
		assertTrue(set.contains(reference))
		println "********** test_get **********"
		set.forEach { TCTOReference ref ->
			assertEquals(testCaseId, ref.testCaseId())
			assertEquals(testObjectId, ref.testObjectGist().id())
			println "ref.testCaseId()=${ref.testCaseId()}"
			println "ref.testObjectGist().id()=${ref.testObjectGist().id()}"
		}
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
		println JsonOutput.prettyPrint(db.toJson())
	}
}
