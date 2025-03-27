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
public class SearchableTextTest {

	private Path TC1 = Paths.get(".").resolve("Scripts/main/TC1/Script1677544889443.groovy")

	@Test
	void test_searchText() {
		println "********** test_searchText **********"
		SearchableText searchableText = new SearchableText(TC1)
		String pattern = "Page_CURA Healthcare Service/a_Make Appointment"
		List<TextSearchResult> list = searchableText.searchText(pattern, false)
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
		println JsonOutput.prettyPrint(sb.toString())
	}

	@Test
	void test_toJson() {
		SearchableText searchableText = new SearchableText(TC1)
		String json = searchableText.toJson()
		println "********** test_toJson **********"
		println json
	}
}
