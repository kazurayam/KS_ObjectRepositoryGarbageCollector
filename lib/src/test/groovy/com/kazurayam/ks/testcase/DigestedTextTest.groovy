package com.kazurayam.ks.testcase

import com.kazurayam.ks.reporting.Shorthand
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

@RunWith(JUnit4.class)
class DigestedTextTest {

	private DigestedText dText

	@Before
	void setup() {
		dText = new DigestedText()
		DigestedLine dline = new DigestedLine.Builder("Hello, new world!", 1)
				.pattern("new", false)
				.matchFound(8, 10)
				.build()
		dText.add(dline)
	}

	@Test
	void test_size() {
		assertEquals(1, dText.size())
	}

	@Test
	void test_toJson() {
		String json = dText.toJson()
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_toJson.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue(json.contains("Hello"))
	}
}
