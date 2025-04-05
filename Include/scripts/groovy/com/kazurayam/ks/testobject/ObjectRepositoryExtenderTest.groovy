package com.kazurayam.ks.testobject

import com.kazurayam.ks.reporting.Shorthand

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static org.junit.Assert.*

import java.nio.file.Path
import java.nio.file.Paths

import org.junit.BeforeClass
import org.junit.FixMethodOrder;
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters;

import com.kazurayam.ks.configuration.RunConfigurationConfigurator
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import internal.GlobalVariable
import groovy.json.JsonOutput

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)
class ObjectRepositoryExtenderTest {

	private static final Path objectRepository = Paths.get("./Object Repository")

	@BeforeClass
	static void beforeClass() {
		RunConfigurationConfigurator.configure()
		//
		ExtendedObjectRepository eor = new ExtendedObjectRepository(objectRepository)
		new ObjectRepositoryExtender(eor).apply()
	}

	@Test
	void test_getTestObjectIdList() {
		List<TestObjectId> list = ObjectRepository.getTestObjectIdList()
		assertTrue(list.size() > 0)
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_getTestObjectIdList.txt").build()
		StringBuilder sb = new StringBuilder()
		list.forEach { toi ->
			sb.append(toi.getValue())
			sb.append("\n")
		}
		sh.write(sb.toString())
	}

	@Test
	void test_getTestObjectIdList_filterByString() {
		List<TestObjectId> list = ObjectRepository.getTestObjectIdList("button_", false)
		assertTrue(list.size() > 0)
	}

	@Test
	void test_jsonifyTestObjectIdList() {
		String json = ObjectRepository.jsonifyTestObjectIdList()
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_jsonifyTestObjectIdList.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue(json.contains("a_Make Appointment"))
	}

	@Test
	void test_jsonifyTestObjectIdList_filterByString() {
		String json = ObjectRepository.jsonifyTestObjectIdList("button_")
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_jsonifyTestObjectIdList.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue(json.contains("button_Book Appointment"))
	}

	//-----------------------------------------------------------------

	@Test
	void test_getTestObjectEssenceList() {
		List<TestObjectEssence> list = ObjectRepository.getTestObjectEssenceList()
		assertTrue(list.size() > 0)
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_getTestObjectEssenceList.txt").build()
		StringBuilder sb = new StringBuilder()
		list.forEach { toe ->
			sb.append(toe.toString())
			sb.append("\n")
		}
		sh.write(sb.toString())
	}

	@Test
	void test_getTestObjectEssenceList_filterByString() {
		List<TestObjectEssence> list = ObjectRepository.getTestObjectEssenceList("button_", false)
		assertTrue(list.size() > 0)
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_getTestObjectEssenceList_filterByString.txt").build()
		StringBuilder sb = new StringBuilder()
		list.forEach { toe ->
			sb.append(toe.toString())
			sb.append("\n")
		}
		sh.write(sb.toString())
	}

	@Test
	void test_jsonifyTestObjectEssenceList() {
		String json = ObjectRepository.jsonifyTestObjectEssenceList()
		assertNotNull(json)
		assertTrue(json.contains("a_Make Appointment"))
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_jsonifyTestObjectEssenceList.json").build()
		sh.write(JsonOutput.prettyPrint(json))
	}

	@Test
	void test_jsonifyTestObjectEssenceList_filterByString() {
		String json = ObjectRepository.jsonifyTestObjectEssenceList("button_", false)
		assertNotNull(json)
		assertTrue(json.contains("button_Book Appointment"))
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_jsonifyTestObjectEssenceList_filterByString.json").build()
		sh.write(JsonOutput.prettyPrint(json))
	}

	//-----------------------------------------------------------------

	@Test
	void test_getLocatorIndex() {
		LocatorIndex result = ObjectRepository.getLocatorIndex()
		assertEquals(13, result.size())
	}

	@Test
	void test_getLocatorIndex_filterByRegex() {
		String pattern = "btn-(.+)-appointment"
		LocatorIndex result = ObjectRepository.getLocatorIndex(pattern, true)
		StringBuilder sb = new StringBuilder()
		result.iterator().each { entry ->
			sb.append(entry.key.toString() + "\n\t" + entry.value.toString())
			sb.append("\n")
		}
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_getLocatorIndex_filterByRegex.txt").build()
		sh.write("pattern: ${pattern}", sb.toString())
		assertEquals(2, result.size())
	}

	@Test
	void test_getLocatorIndex_filterByString() {
		String pattern = "select"
		LocatorIndex result = ObjectRepository.getLocatorIndex("select", false)
		StringBuilder sb = new StringBuilder()
		result.iterator().each { entry ->
			sb.append(entry.key.toString() + "\n\t" + entry.value.toString())
			sb.append("\n")
		}
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_getLocatorIndex_filterByString.txt").build()
		sh.write("pattern: ${pattern}", sb.toString())
		assertEquals(1, result.size())
	}

	@Test
	void test_jsonifyLocatorIndex() {
		String json = ObjectRepository.jsonifyLocatorIndex()
		assertNotNull(json)
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_jsonifyLocatorIndex.json").build()
		sh.write(JsonOutput.prettyPrint(json))
	}

	@Test
	void test_jsonifyLocatorIndex_filterByString() {
		String json = ObjectRepository.jsonifyLocatorIndex("//a[@id")   // default: isRegex = false
		assertNotNull(json)
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
				.fileName("test_jsonifyLocatorIndex_filterByString.json").build()
		sh.write(JsonOutput.prettyPrint(json))
	}

	/**
	 * test if ObjectRepository's ordinary method works
	 */
	@Test
	void test_findTestObject() {
		TestObject tObj = ObjectRepository.findTestObject("main/Page_CURA Healthcare Service/button_Login")
		assertNotNull('ObjectRepository.findTestObject("main/Page_CURA Healthcare Service/button_Login") returned null', tObj)
	}
}
