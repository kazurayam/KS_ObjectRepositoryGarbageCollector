package com.kazurayam.ks.testobject

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4.class)

public class TestObjectIdTest {

	private TestObjectId testObjectId

	@Before
	public void setup() {
		testObjectId = new TestObjectId("Page/foo")
	}

	@Test
	void test_value() {
		assertEquals("Page/foo", testObjectId.value())
	}

	@Test
	void test_toJson() {
		println testObjectId.toJson()
		assertEquals("{\"TestObjectId\":\"Page/foo\"}", testObjectId.toJson())
	}
}

