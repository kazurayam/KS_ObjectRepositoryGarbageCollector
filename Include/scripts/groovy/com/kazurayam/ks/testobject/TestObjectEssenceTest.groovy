package com.kazurayam.ks.testobject

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import groovy.json.JsonOutput

@RunWith(JUnit4.class)
public class TestObjectEssenceTest {

	private TestObjectEssence essence

	TestObjectId testObjectId = new TestObjectId("testObjectX")
	String method = "BASIC"
	Locator locator = new Locator('//div[@id="main"]')

	@Before
	void setup() {
		essence = new TestObjectEssence(testObjectId, method, locator)
	}

	@Test
	void test_testObjectId() {
		assertEquals(testObjectId, essence.testObjectId())
	}

	@Test
	void test_method() {
		assertEquals(method, essence.method())
	}

	@Test
	void test_locator() {
		assertEquals(locator, essence.locator())
	}

	@Test
	void test_toString() {
		String s = essence.toString()
		println "********** TestObjectEssenceTest#toString **********"
		println s
		assertTrue("toString() should contain 'testObjectX'",
				s.contains('testObjectX')
				)
		assertTrue('toString() should contain //div[@id=\\"main\\"]',
				s.contains('//div[@id=\\"main\\"]')
				)
	}
}
