package com.kazurayam.ks.testobject

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kazurayam.ks.reporting.Shorthand

import groovy.json.JsonOutput
import internal.GlobalVariable

@RunWith(JUnit4.class)
public class LocatorTest {

	private Locator locator

	@Before
	public void setup() {
		locator = new Locator("//a")
	}

	@Test
	void test_value() {
		assertEquals("//a", locator.getValue())
	}

	@Test
	void test_toJson() {
		String json = JsonOutput.prettyPrint(locator.toJson())
		Shorthand sh = new Shorthand .Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_toJson.json").build()
		sh.write(json)
		assertEquals('{\"Locator\":\"//a\"}', locator.toJson())
	}
}
