package com.kazurayam.ks.testcase

import com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver
import com.kazurayam.ks.reporting.Shorthand
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.nio.file.Files
import java.nio.file.Path

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

@RunWith(JUnit4.class)
public class ScriptsDecoratorTest {

	private static Path scriptsDir

	@BeforeClass
	public static void beforeClass() {
		scriptsDir = KatalonProjectDirectoryResolver.getProjectDir().resolve("Scripts")
		assert Files.exists(scriptsDir)
	}

	@Test
	public void test_getGroovyFile_include_all() {
		ScriptsDecorator decorator = new ScriptsDecorator.Builder(scriptsDir).build()
		List<Path> groovyFiles = decorator.getGroovyFiles()
		assertTrue("groovyFiles is empty", groovyFiles.size() > 0)
		//
		StringBuilder sb = new StringBuilder()
		groovyFiles.each { file ->
			assertTrue(file.toString().endsWith(".groovy"))
			sb.append(file.toString() + "\n")
		}
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_getGroovyFiles_include_all.txt").build()
		sh.write(sb.toString())
		//
		assertEquals(28, groovyFiles.size())
	}

	@Test
	public void test_getGroovyFile_include_main_only() {
		ScriptsDecorator decorator = new ScriptsDecorator.Builder(scriptsDir)
				.includeFolder("**/main/*").build()
		List<Path> groovyFiles = decorator.getGroovyFiles()
		assertEquals(2, groovyFiles.size())
	}
}
