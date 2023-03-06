package com.kazurayam.ks.testcase

import static org.junit.Assert.*

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import groovy.json.JsonOutput


@RunWith(JUnit4.class)
public class TestCaseScriptsVisitorTest {

	private static final Path scriptsDir = Paths.get("./Scripts")
	private static final String subpath = "main"
	private TestCaseScriptsVisitor visitor

	@Test
	void test_getScripts() throws IOException {
		visitor = new TestCaseScriptsVisitor(scriptsDir)
		Files.walkFileTree(scriptsDir.resolve(subpath), visitor)
		List<Path> list = visitor.getGroovyFiles()
		assertTrue(list.size() > 0)
		println JsonOutput.prettyPrint(toJson(list))
		list.forEach { p ->
			assertTrue("`${p.toString()}` is expected to starts with 'main'", p.toString().startsWith("main"))
		}
	}

	private String toJson(List<Path> it) {
		StringBuilder sb = new StringBuilder()
		String sep = ""
		sb.append("[")
		it.forEach( { p ->
			sb.append(sep)
			sb.append("\"")
			sb.append(p.toString())
			sb.append("\"")
			sep = ", "
		})
		sb.append("]")
		return sb.toString()
	}
}


/*
 [
 junit4/com.kazurayam.ks.testcase/TestCaseScriptsVisitorTestRunner/Script1677756819625.groovy,
 junit4/com.kazurayam.ks.testobject/BiMatcherTestRunner/Script1677660209410.groovy,
 junit4/com.kazurayam.ks.testobject/ObjectRepositoryExtensionTestRunner/Script1677660209413.groovy,
 junit4/com.kazurayam.ks.testobject/ObjectRepositoryVisitorTestRunner/Script1677660209416.groovy,
 junit4/junittutorial/CalculatorTestRunner/Script1677546841672.groovy, 
 main/TC1/Script1677544889443.groovy, 
 sampleUse/step1/Script1677677313086.groovy, 
 sampleUse/step2/Script1677677321383.groovy, 
 sampleUse/step3/Script1677677327691.groovy, 
 sampleUse/step4/Script1677677335380.groovy, 
 sampleUse/step5/Script1677677343760.groovy, 
 sampleUse/step6/Script1677677351677.groovy, 
 sampleUse/step7/Script1677677358728.groovy, 
 sampleUse/step8/Script1677677364766.groovy, 
 sampleUse/step9/Script1677677373080.groovy
 ]
 */