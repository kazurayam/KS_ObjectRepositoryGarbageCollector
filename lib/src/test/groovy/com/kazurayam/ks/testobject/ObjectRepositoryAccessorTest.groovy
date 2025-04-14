package com.kazurayam.ks.testobject

import com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver
import com.kazurayam.ks.reporting.Shorthand
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.nio.file.Files
import java.nio.file.Path

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

@RunWith(JUnit4.class)
public class ObjectRepositoryAccessorTest {

	private static Path objectRepositoryDir

	@BeforeClass
	public static void beforeClass() {
		objectRepositoryDir = KatalonProjectDirectoryResolver.getProjectDir().resolve("Object Repository")
		assert Files.exists(objectRepositoryDir)
	}

	@Before
	public void setup() {
	}

	@Test
	public void test_getIncludedFiles() {
		ObjectRepositoryAccessor accessor = 
				new ObjectRepositoryAccessor.Builder(objectRepositoryDir).build()
		String[] includedFiles = accessor.getIncludedFiles()
		StringBuilder sb = new StringBuilder()
		for (int i = 0; i < includedFiles.length; i++) {
			sb.append(includedFiles[i])
			sb.append("\n")
		}
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_getIncludedFiles.txt").build()
		sh.write(sb.toString())
		assertTrue(includedFiles.length > 0)
	}
	
	@Test
	public void test_getIncludedFiles_mutiple() {
		ObjectRepositoryAccessor accessor =
				new ObjectRepositoryAccessor.Builder(objectRepositoryDir)
					.includeFile("**/main/**/*.rs")
					.includeFile("**/misc/**/*.rs")
					.build()
		String[] includedFiles = accessor.getIncludedFiles()
		assertEquals(16, includedFiles.length)
	}

	@Test
	public void test_getTestObjectIdList() {
		ObjectRepositoryAccessor accessor =
				new ObjectRepositoryAccessor.Builder(objectRepositoryDir).build()
		List<TestObjectId> list = accessor.getTestObjectIdList()
		StringBuilder sb = new StringBuilder()
		list.each { id ->
			sb.append(id.getValue())
			sb.append("\n")
		}
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_getTestObjectIdList.txt").build()
		sh.write(sb.toString())
		assertTrue(list.size() > 0)
	}

	@Test
	public void test_getRsFiles() {
		ObjectRepositoryAccessor accessor =
				new ObjectRepositoryAccessor.Builder(objectRepositoryDir).build()
		List<Path> rsFiles = accessor.getRsFiles()
		assertTrue("rsFiles is empty", rsFiles.size() > 0)
		StringBuilder sb = new StringBuilder()
		rsFiles.each { file ->
			assertTrue(file.toString().endsWith(".rs"))
			sb.append(file.toString() + "\n")
		}
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_getRsFiles.txt").build()
		sh.write(sb.toString())
		assertEquals(16, rsFiles.size())
	}
}
