package com.kazurayam.ks.testobject

import com.kazurayam.ks.reporting.Shorthand
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

@RunWith(JUnit4.class)
public class TestObjectIdTest {

	private TestObjectId testObjectId

	@Before
	public void setup() {
		testObjectId = new TestObjectId("Page/foo")
	}

	@Test
	void test_value() {
		assertEquals("Page/foo", testObjectId.getValue())
	}

	@Test
	void test_toJson() {
		String json = testObjectId.toJson()
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_toJson.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue(json.contains("Page/foo"))
	}
}

