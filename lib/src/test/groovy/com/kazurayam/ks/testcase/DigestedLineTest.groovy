package com.kazurayam.ks.testcase

import com.kazurayam.ks.reporting.Shorthand
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import static org.junit.Assert.assertEquals

@RunWith(JUnit4.class)
public class DigestedLineTest {

	private DigestedLine dLine

	@Before
	void setup() {
		dLine = new DigestedLine.Builder("Hello, new world!", 1)
				.pattern("new", false)
				.matchFound(8, 10)
				.build()
	}

	@Test
	void test_smoke() {
		assertEquals("Hello, new world!", dLine.getLine())
		assertEquals(1, dLine.getLineNo())
		assertEquals("new", dLine.getPattern())
		assertEquals(false, dLine.isRegex())
		assertEquals(8, dLine.getMatchAt())
		assertEquals(10, dLine.getMatchEnd())
		assertEquals(true, dLine.isMatched())
	}

	@Test
	void test_toJson() {
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_toJson.json").build()
		sh.write(JsonOutput.prettyPrint(dLine.toJson()))
	}
}
