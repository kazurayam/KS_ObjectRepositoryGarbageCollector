package com.kazurayam.ks.text


import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue


@RunWith(JUnit4.class)
class RegexOptedTextMateherTest {

	private RegexOptedTextMatcher matcher

	@Test
	void test_isRegex() {
		RegexOptedTextMatcher bim = new RegexOptedTextMatcher("button\\[@id", true)
		assertTrue(bim.found("//button[@id='foo']"))
		assertFalse(bim.found("//a[@id='bar']"))
	}

	@Test
	void test_isNotRegex() {
		RegexOptedTextMatcher bim = new RegexOptedTextMatcher("button[@id", false)
		assertTrue(bim.found("//button[@id='foo']"))
		assertFalse(bim.found("//a[@id='bar']"))
	}

	/**
	 * isRegex should default to false
	 */
	@Test
	void test_default() {
		RegexOptedTextMatcher bim = new RegexOptedTextMatcher("button[@id")
		assertTrue(bim.found("//button[@id='foo']"))
		assertFalse(bim.found("//a[@id='bar']"))
	}
}
