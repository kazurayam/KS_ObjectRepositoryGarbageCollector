package com.kazurayam.ks.testcase

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import groovy.json.JsonOutput

@RunWith(JUnit4.class)
public class ExternalReferencesTest {

	private TextSearchResult tsr
	private ExternalReferences collection

	private static String pattern = "new"
	private static Boolean isRegex = false
	private static String sourcePath = "src/main/my/hello.groovy"

	@Before
	void setup() {
		collection = new ExternalReferences()
		tsr = new TextSearchResult.Builder("Hello, new world!", 1)
				.pattern(pattern, isRegex)
				.matchFound(8, 10)
				.build()
		collection.put(sourcePath, tsr)
	}

	@Test
	void test_keySet() {
		assertEquals(1, collection.keySet().size())
	}

	@Test
	void test_containsKey() {
		assertTrue(collection.containsKey(sourcePath))
	}

	@Test
	void test_get() {
		assertEquals(1, collection.get(sourcePath).size())
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
