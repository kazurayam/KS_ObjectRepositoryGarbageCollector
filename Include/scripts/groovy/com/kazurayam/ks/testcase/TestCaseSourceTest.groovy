package com.kazurayam.ks.testcase

import static org.junit.Assert.*

import java.nio.file.Path
import java.nio.file.Paths

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kms.katalon.core.configuration.RunConfiguration

import groovy.json.JsonOutput

@RunWith(JUnit4.class)
public class TestCaseSourceTest {

	private Path TC1 = Paths.get(RunConfiguration.getProjectDir()).resolve("Scripts/main/TC1/Script1677544889443.groovy")

	@Test
	void test_smoke() {
		TestCaseSource tcs = new TestCaseSource(TC1)
		String pattern = "Page_CURA Healthcare Service/a_Make Appointment"
		List<TextSearchResult> tsrList = tcs.searchText(pattern, false)
		assertEquals(1, tsrList.size())
		StringBuilder sb = new StringBuilder()
		String sep = ""
		tsrList.forEach { tsr ->
			sb.append(sep)
			sb.append(tsr.toJson())
			sep = ","
		}
		println JsonOutput.prettyPrint(sb.toString())
	}
}
