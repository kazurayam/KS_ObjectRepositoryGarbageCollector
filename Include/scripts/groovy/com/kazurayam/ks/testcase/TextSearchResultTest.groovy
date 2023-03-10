package com.kazurayam.ks.testcase

import static org.junit.Assert.*

import org.junit.Test
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import groovy.json.JsonOutput

@RunWith(JUnit4.class)
public class TextSearchResultTest {

	private TextSearchResult tsr

	@Before
	void setup() {
		tsr = new TextSearchResult.Builder("Hello, new world!", 1)
				.pattern("new", false)
				.matchFound(8, 10)
				.build()
	}

	@Test
	void test_smoke() {
		assertEquals("Hello, new world!", tsr.line())
		assertEquals(1, tsr.lineNo())
		assertEquals("new", tsr.pattern())
		assertEquals(false, tsr.isRegex())
		assertEquals(8, tsr.matchAt())
		assertEquals(10, tsr.matchEnd())
		assertEquals(true, tsr.hasMatch())
	}

	@Test
	void test_toJson() {
		println "********** test_toJson **********"
		println tsr.toJson()
	}

}
