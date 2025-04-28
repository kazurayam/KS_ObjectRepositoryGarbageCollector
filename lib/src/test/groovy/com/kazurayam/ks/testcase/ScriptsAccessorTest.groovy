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
class ScriptsAccessorTest {

	private static Path scriptsDir
	private ScriptsAccessor accessor

	@BeforeClass
	public static void beforeClass() {
		scriptsDir = KatalonProjectDirectoryResolver.getProjectDir().resolve("Scripts")
		assert Files.exists(scriptsDir)
	}


	@Test
	public void test_getGroovyFiles_include_all() {
		accessor = new ScriptsAccessor.Builder(scriptsDir).build()
		List<Path> groovyFiles = accessor.getGroovyFiles()
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
		assertEquals(27, groovyFiles.size())
	}

	@Test
	public void test_getGroovyFiles_include_demo_and_main() {
		accessor = new ScriptsAccessor.Builder(scriptsDir)
				.includeFile("demo/**/*.groovy")
				.includeFile("main/**/*.groovy")
				.build()
		List<Path> groovyFiles = accessor.getGroovyFiles()
		assertEquals(24, groovyFiles.size())
	}
}
