package com.kazurayam.ks.testcase

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4.class)

public class TestCaseIdTest {

	private TestCaseId testCaseId

	@Before
	public void setup() {
		testCaseId = new TestCaseId("main/TC1")
	}

	@Test
	void test_value() {
		assertEquals("main/TC1", testCaseId.value())
	}

	@Test
	void test_toJson() {
		println testCaseId.toJson()
		assertEquals("{\"TestCaseId\":\"main/TC1\"}", testCaseId.toJson())
	}
}
