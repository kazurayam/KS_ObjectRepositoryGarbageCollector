package com.kazurayam.ks.testobject

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4.class)
public class TestObjectGistTest {
	
	private TestObjectGist gist
	
	String id = "testObjectX"
	String method = "BASIC"
	String locator = "//div[@id=\"main\"]"
	
	@Before
	void setup() {
		gist = new TestObjectGist(id, method, locator)
	}

	@Test
	void test_id() {
		assertEquals(id, gist.id())
	}

	@Test
	void test_method() {
		assertEquals(method, gist.method())
	}

	@Test
	void test_locator() {
		assertEquals(locator, gist.locator())
	}
	
	@Test
	void test_toString() {
		println gist.toString()
	}

}
