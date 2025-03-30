package com.kazurayam.ks.testcase

import static org.junit.Assert.*

import java.nio.file.Path
import java.nio.file.Paths

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kms.katalon.core.configuration.RunConfiguration

import groovy.json.JsonOutput
import com.kazurayam.ks.reporting.Shorthand

import internal.GlobalVariable

@RunWith(JUnit4.class)
public class SearchableTextTest {

	private Path TC1 = Paths.get(".").resolve("Scripts/main/TC1/Script1677544889443.groovy")

	@Test
	void test_searchText() {
		SearchableText searchableText = new SearchableText(TC1)
		String pattern = "Page_CURA Healthcare Service/a_Make Appointment"
		List<DigestedLine> list = searchableText.searchText(pattern, false)
		assertEquals(1, list.size())
		StringBuilder sb = new StringBuilder()
		sb.append("[")
		String sep = ""
		list.forEach { tsr ->
			sb.append(sep)
			sb.append(tsr.toJson())
			sep = ","
		}
		sb.append("]")
		//
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
						.fileName("test_searchText.json").build()
		String json = JsonOutput.prettyPrint(sb.toString())
		sh.write(json)
	}

	@Test
	void test_toJson() {
		SearchableText searchableText = new SearchableText(TC1)
		String json = searchableText.toJson()
		//
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
						.fileName("test_toJson.json").build()
		sh.write(json)
	}
}
