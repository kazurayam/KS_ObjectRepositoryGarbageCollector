package com.kazurayam.ks.testobject

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import groovy.json.JsonOutput

@RunWith(JUnit4.class)
public class TestObjectGistTest {

	private TestObjectGist gist

	TestObjectId testObjectId = new TestObjectId("testObjectX")
	String method = "BASIC"
	Locator locator = new Locator('//div[@id="main"]')

	@Before
	void setup() {
		gist = new TestObjectGist(testObjectId, method, locator)
	}

	@Test
	void test_testObjectId() {
		assertEquals(testObjectId, gist.testObjectId())
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
		String s = gist.toString()
		println "********** TestObjectGistTest#toString **********"
		println s
		assertTrue("toString() should contain 'testObjectX'", 
					s.contains('testObjectX')
					)
		assertTrue('toString() should contain //div[@id=\\"main\\"]', 
					s.contains('//div[@id=\\"main\\"]')
			)
	}
}
