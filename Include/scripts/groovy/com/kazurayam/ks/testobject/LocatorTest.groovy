package com.kazurayam.ks.testobject


import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4.class)

public class LocatorTest {

	private Locator locator

	@Before
	public void setup() {
		locator = new Locator("//a")
	}

	@Test
	void test_value() {
		assertEquals("//a", locator.value())
	}

	@Test
	void test_toJson() {
		println locator.toJson()
		assertEquals('{\"Locator\":\"//a\"}', locator.toJson())
	}
}
