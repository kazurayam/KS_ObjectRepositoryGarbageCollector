package com.kazurayam.ks.testobject

import static org.junit.Assert.*

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import groovy.json.JsonOutput
import com.kazurayam.ks.reporting.Shorthand

import internal.GlobalVariable
import com.kms.katalon.core.configuration.RunConfiguration

@RunWith(JUnit4.class)
public class ObjectRepositoryAccessorTest {

	private static Path objectRepositoryDir
	private ObjectRepositoryAccessor accessor

	@BeforeClass
	public static void beforeClass() {
		objectRepositoryDir = Paths.get(RunConfiguration.getProjectDir()).resolve("Object Repository")
		assert Files.exists(objectRepositoryDir)
	}

	@Before
	public void setup() {
		accessor = new ObjectRepositoryAccessor(objectRepositoryDir)
	}

	@Test
	public void test_getIncludedFiles() {
		String[] includedFiles = accessor.getIncludedFiles()
		StringBuilder sb = new StringBuilder()
		for (int i = 0; i < includedFiles.length; i++) {
			sb.append(includedFiles[i])
			sb.append("\n")
		}
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
						.fileName("test_getIncludedFiles.txt").build()
		sh.write(sb.toString())
		assertTrue(includedFiles.size() > 0)
	}
	
	@Test
	public void test_getTestObjectIdList() {
		List<TestObjectId> list = accessor.getTestObjectIdList()
		StringBuilder sb = new StringBuilder()
		list.each { id ->
			sb.append(id.getValue())
			sb.append("\n")
		}
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
						.fileName("test_getTestObjectIdList.txt").build()
		sh.write(sb.toString())
		assertTrue(list.size() > 0)
	}
	
	@Test
	public void test_getRsFiles() {
		List<Path> rsFiles = accessor.getRsFiles()
		assertTrue("rsFiles is empty", rsFiles.size() > 0)
		StringBuilder sb = new StringBuilder()
		rsFiles.each { file ->
			assertTrue(file.toString().endsWith(".rs"))
			sb.append(file.toString() + "\n")
		}
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_getRsFiles.txt").build()
		sh.write(sb.toString())
		assertEquals(15, rsFiles.size())
	}
}
