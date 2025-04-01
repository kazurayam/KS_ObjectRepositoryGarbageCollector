package com.kazurayam.ks.testobject

import static org.junit.Assert.*

import java.nio.file.Path
import java.nio.file.Paths

import org.junit.Before
import org.junit.FixMethodOrder;
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters;

import com.kazurayam.ks.reporting.Shorthand

import groovy.json.JsonOutput
import internal.GlobalVariable

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)

public class ExtendedObjectRepositoryTest {

	private ExtendedObjectRepository instance

	@Before
	void setup() {
		Path objectRepositoryDir = Paths.get(".").resolve("Object Repository")
		instance = new ExtendedObjectRepository(objectRepositoryDir)
	}

	@Test
	void test_getTestObjectIdList() {
		List<TestObjectId> list = instance.getTestObjectIdList()
		assertTrue( list.size() > 0 )
	}

	@Test
	void test_getTestObjectIdList_filterByString() {
		String pattern = "button_"
		List<TestObjectId> list = instance.getTestObjectIdList(pattern, false)
		assertTrue( list.size() > 0 )
	}

	@Test
	void test_getTestObjectIdList_filterByRegex() {
		String pattern = "button_(\\w+)"
		List<TestObjectId> list = instance.getTestObjectIdList(pattern, true)
		assertTrue( list.size() > 0 )
	}

	//-----------------------------------------------------------------

	@Test
	void test_jsonifyTestObjectIdList() {
		String json = instance.jsonifyTestObjectIdList()
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_jsonifyTestObjectIdList.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue(json.contains("a_Make Appointment"))
	}

	@Test
	void test_jsonifyTestObjectIdList_filterByString() {
		String pattern = "button_"
		String json = instance.jsonifyTestObjectIdList(pattern, false)
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_jsonifyTestObjectIdList_filterByString.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue(json.contains("button_Login"))
	}

	@Test
	void test_jsonifyTestObjectIdList_filterByRegex() {
		String pattern = "button_(\\w+)"
		String json = instance.jsonifyTestObjectIdList(pattern, true)
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_jsonifyTestObjectIdList_filterByRegex.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue(json.contains("button_Login"))
	}

	//-----------------------------------------------------------------

	@Test
	void test_getTestObjectEssenceList() {
		String pattern = ""
		Boolean isRegex = false
		List<Map<String, String>> result = instance.getTestObjectEssenceList(pattern, isRegex)
		assertTrue( result.size() > 0 )
	}

	@Test
	void test_getTestObjectEssenceList_filterByRegex() {
		String pattern = "button_(\\w+)"
		Boolean isRegex = true
		List<Map<String, String>> result = instance.getTestObjectEssenceList(pattern, isRegex)
		assertTrue( result.size() > 0 )
	}

	@Test
	void test_jsonifyTestObjectEssenceList() {
		String pattern = ""
		Boolean isRegex = false
		String json = instance.jsonifyTestObjectEssenceList(pattern, isRegex)
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_jsonifyTestObjectEssenceList.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue("json should contain 'a_Make Appointment'", json.contains("a_Make Appointment"))
	}

	@Test
	void test_jsonifyTestObjectEssenceList_filterByRegex() {
		String pattern = "button_(\\w+)"
		Boolean isRegex = true
		String json = instance.jsonifyTestObjectEssenceList(pattern, isRegex)
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_jsonifyTestObjectEssenceList_filterByRegex.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue(json.contains("button_Login"))
	}

	//-----------------------------------------------------------------

	@Test
	void test_getAllTestObjectIdSet() {
		Set<TestObjectId> allTOI = instance.getAllTestObjectIdSet()
		StringBuilder sb = new StringBuilder()
		allTOI.forEach({ toi ->
			sb.append(toi)
			sb.append("\n")
			assertNotNull(toi.getValue())
			assertNotEquals("", toi.getValue())
		})
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_getAllTestObjectIdSet.txt").build()
		sh.write(sb.toString())
	}

	//-----------------------------------------------------------------

	@Test
	void test_getLocatorIndex() {
		LocatorIndex locatorIndex = instance.getLocatorIndex()
		assertNotNull(locatorIndex)
		assertTrue(locatorIndex.size() > 0)
	}

	@Test
	void test_jsonifyLocatorIndex() {
		String json = instance.jsonifyLocatorIndex()
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_jsonifyLocatorIndex.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue(json.contains("a_Make Appointment"))
	}
}
