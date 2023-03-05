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

import groovy.json.JsonOutput

@RunWith(JUnit4.class)
public class DatabaseTest {

	private TextSearchResult tsr
	private Database collection

	private static String pattern = "new"
	private static Boolean isRegex = false
	private static TestCaseId id = new TestCaseId("src/main/my/hello.groovy")

	@Before
	void setup() {
		collection = new Database()
		tsr = new TextSearchResult.Builder("Hello, new world!", 1)
				.pattern(pattern, isRegex)
				.matchFound(8, 10)
				.build()
		collection.put(id, tsr)
	}

	@Test
	void test_keySet() {
		assertEquals(1, collection.keySet().size())
	}

	@Test
	void test_containsKey() {
		assertTrue(collection.containsKey(id))
	}

	@Test
	void test_get() {
		assertEquals(1, collection.get(id).size())
	}

	@Test
	void test_toString() {
		println collection.toString()
	}

	@Test
	void test_toJson() {
		println JsonOutput.prettyPrint(collection.toJson())
	}
}
