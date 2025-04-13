package com.kazurayam.ks.testobject

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import groovy.json.JsonOutput
import com.kazurayam.ks.reporting.Shorthand

import internal.GlobalVariable

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
		assertEquals(testObjectId, essence.getTestObjectId())
	}

	@Test
	void test_method() {
		assertEquals(method, essence.getMethod())
	}

	@Test
	void test_locator() {
		assertEquals(locator, essence.getLocator())
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

	@Test
	void test_toJson() {
		String json = essence.toJson()
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName("test_toJson.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue(json.contains("testObjectX"))
	}
}
