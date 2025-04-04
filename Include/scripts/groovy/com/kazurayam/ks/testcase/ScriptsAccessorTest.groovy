package com.kazurayam.ks.testcase

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
public class ScriptsAccessorTest {

	private static Path scriptsDir
	private ScriptsAccessor accessor

	@BeforeClass
	public static void beforeClass() {
		scriptsDir = Paths.get(RunConfiguration.getProjectDir()).resolve("Scripts")
		assert Files.exists(scriptsDir)
	}

	@Before
	public void setup() {
		accessor = new ScriptsAccessor(scriptsDir)
	}

	@Test
	public void test_getGroovyFiles() {
		List<Path> groovyFiles = accessor.getGroovyFiles()
		assertTrue("groovyFiles is empty", groovyFiles.size() > 0)
		StringBuilder sb = new StringBuilder()
		groovyFiles.each { file ->
			assertTrue(file.toString().endsWith(".groovy"))
			sb.append(file.toString() + "\n")
		}
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_getGroovyFiles.txt").build()
		sh.write(sb.toString())
		//
		assertEquals(39, groovyFiles.size())
	}
}
