package com.kazurayam.ks.testobject

import static org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kazurayam.ks.testobject.BiMatcher


@RunWith(JUnit4.class)
public class BiMatcherTest {
	
	private BiMatcher bim
	
	@Test
	public void test_isRegex() {
		BiMatcher bim = new BiMatcher("button\\[@id", true)
		assertTrue(bim.matches("//button[@id='foo']"))
		assertFalse(bim.matches("//a[@id='bar']"))
	}
	
	@Test
	public void test_isNotRegex() {
		BiMatcher bim = new BiMatcher("button[@id", false)
		assertTrue(bim.matches("//button[@id='foo']"))
		assertFalse(bim.matches("//a[@id='bar']"))
	}
	
	/**
	 * isRegex should default to false
	 */
	@Test
	public void test_default() {
		BiMatcher bim = new BiMatcher("button[@id")
		assertTrue(bim.matches("//button[@id='foo']"))
		assertFalse(bim.matches("//a[@id='bar']"))
	}
	
	
}
