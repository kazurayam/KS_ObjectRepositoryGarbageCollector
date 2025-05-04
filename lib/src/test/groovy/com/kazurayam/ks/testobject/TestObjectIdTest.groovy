package com.kazurayam.ks.testobject

import com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver
import com.kazurayam.ks.reporting.Shorthand
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

@RunWith(JUnit4.class)
class TestObjectIdTest {

	private static Path objectRepositoryDir
	private TestObjectId testObjectId
	private static final String toiString =
			'main/Page_CURA Healthcare Service/a_Make Appointment'

	@BeforeClass
	static void beforeClass() {
		objectRepositoryDir =
				KatalonProjectDirectoryResolver.getProjectDir()
						.resolve("Object Repository")
		assert Files.exists(objectRepositoryDir)
	}

	@Before
	void setup() {
		Path relativePath = Paths.get(toiString + '.rs')
		testObjectId = new TestObjectId(relativePath)
	}

	@Test
	void test_value() {
		assertEquals(toiString, testObjectId.getValue())
	}

	@Test
	void test_getRelativePath() {
		Path relativePath = testObjectId.getRelativePath()
		assertFalse(relativePath.isAbsolute())
		Path resolved = objectRepositoryDir.resolve(relativePath)
		assertTrue(Files.exists(resolved))
	}

	@Test
	void test_toJson() {
		String json = testObjectId.toJson()
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_toJson.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue(json.contains(toiString))
	}
}

