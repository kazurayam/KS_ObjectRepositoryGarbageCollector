package com.kazurayam.ks.testcase

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import com.kazurayam.ks.reporting.Shorthand

import internal.GlobalVariable


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
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
						.fileName("test_toJson.json").build()
		sh.write(testCaseId.toJson())
		assertEquals("\"main/TC1\"", testCaseId.toJson())
	}
}
