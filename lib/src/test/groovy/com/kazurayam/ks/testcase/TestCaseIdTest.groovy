package com.kazurayam.ks.testcase

import com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver
import com.kazurayam.ks.reporting.Shorthand
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.nio.file.Path

import static org.junit.Assert.assertEquals

@RunWith(JUnit4.class)

public class TestCaseIdTest {

	private Path projectDir

	@Before
	public void setup() {
		projectDir = KatalonProjectDirectoryResolver.getProjectDir()
	}

	@Test
	void test_value() {
		TestCaseId testCaseId = new TestCaseId("main/TC1")
		assertEquals("main/TC1", testCaseId.getValue())
	}
	
	@Test
	void test_resolveTestCaseId() {
		Path scriptsDir = projectDir.resolve("Scripts")
		Path groovyFile = projectDir.resolve("Scripts/junit4/com.kazurayam.ks.testcase/TestCaseIdTestRunner/Script1678362966349.groovy")
		TestCaseId testCaseId = TestCaseId.resolveTestCaseId(scriptsDir, groovyFile)
		assertEquals("junit4/com.kazurayam.ks.testcase/TestCaseIdTestRunner", testCaseId.getValue())
	}

	@Test
	void test_toJson() {
		TestCaseId testCaseId = new TestCaseId("main/TC1")
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_toJson.json").build()
		sh.write(testCaseId.toJson())
		assertEquals("\"main/TC1\"", testCaseId.toJson())
	}
}
