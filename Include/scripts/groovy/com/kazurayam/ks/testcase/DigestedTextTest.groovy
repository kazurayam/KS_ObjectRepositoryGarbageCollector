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
public class DigestedTextTest {
	
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
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
						.fileName("test_toJson.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue(json.contains("Hello"))
	}
}
