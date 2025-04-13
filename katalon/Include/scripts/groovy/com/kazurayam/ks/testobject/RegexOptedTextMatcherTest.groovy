package com.kazurayam.ks.testobject

import static org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kazurayam.ks.testobject.RegexOptedTextMatcher


@RunWith(JUnit4.class)
public class RegexOptedTextMateherTest {

	private RegexOptedTextMatcher bim

	@Test
	public void test_isRegex() {
		RegexOptedTextMatcher bim = new RegexOptedTextMatcher("button\\[@id", true)
		assertTrue(bim.found("//button[@id='foo']"))
		assertFalse(bim.found("//a[@id='bar']"))
	}

	@Test
	public void test_isNotRegex() {
		RegexOptedTextMatcher bim = new RegexOptedTextMatcher("button[@id", false)
		assertTrue(bim.found("//button[@id='foo']"))
		assertFalse(bim.found("//a[@id='bar']"))
	}

	/**
	 * isRegex should default to false
	 */
	@Test
	public void test_default() {
		RegexOptedTextMatcher bim = new RegexOptedTextMatcher("button[@id")
		assertTrue(bim.found("//button[@id='foo']"))
		assertFalse(bim.found("//a[@id='bar']"))
	}
}
