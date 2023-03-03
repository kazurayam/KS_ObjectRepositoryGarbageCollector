package com.kazurayam.ks.testobject

import static org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kazurayam.ks.testobject.BilingualMatcher


@RunWith(JUnit4.class)
public class BilingualMatcherTest {

	private BilingualMatcher bim

	@Test
	public void test_isRegex() {
		BilingualMatcher bim = new BilingualMatcher("button\\[@id", true)
		assertTrue(bim.matches("//button[@id='foo']"))
		assertFalse(bim.matches("//a[@id='bar']"))
	}

	@Test
	public void test_isNotRegex() {
		BilingualMatcher bim = new BilingualMatcher("button[@id", false)
		assertTrue(bim.matches("//button[@id='foo']"))
		assertFalse(bim.matches("//a[@id='bar']"))
	}

	/**
	 * isRegex should default to false
	 */
	@Test
	public void test_default() {
		BilingualMatcher bim = new BilingualMatcher("button[@id")
		assertTrue(bim.matches("//button[@id='foo']"))
		assertFalse(bim.matches("//a[@id='bar']"))
	}
}
