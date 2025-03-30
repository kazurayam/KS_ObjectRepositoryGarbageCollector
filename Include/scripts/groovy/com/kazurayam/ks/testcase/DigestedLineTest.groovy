package com.kazurayam.ks.testcase

import static org.junit.Assert.*

import org.junit.Test
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import groovy.json.JsonOutput
import com.kazurayam.ks.reporting.Shorthand

import internal.GlobalVariable

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
		assertEquals("Hello, new world!", dLine.line())
		assertEquals(1, dLine.lineNo())
		assertEquals("new", dLine.pattern())
		assertEquals(false, dLine.isRegex())
		assertEquals(8, dLine.matchAt())
		assertEquals(10, dLine.matchEnd())
		assertEquals(true, dLine.hasMatch())
	}

	@Test
	void test_toJson() {
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_toJson.json").build()
		sh.write(JsonOutput.prettyPrint(dLine.toJson()))
	}
}
