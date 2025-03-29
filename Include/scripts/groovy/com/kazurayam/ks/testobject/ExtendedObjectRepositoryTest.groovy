package com.kazurayam.ks.testobject

import static org.junit.Assert.*

import java.nio.file.Path
import java.nio.file.Paths

import org.junit.Before
import org.junit.BeforeClass
import org.junit.FixMethodOrder;
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters;

import com.kazurayam.ks.reporting.Shorthand
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
		List list = instance.getTestObjectIdList()
		assertTrue( list.size() > 0 )
	}

	@Test
	void test_jsonifyTestObjectIdList_default() {
		String json = instance.jsonifyTestObjectIdList()
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName("test_jsonifyTestObjectIdList_default").build()
		sh.write(json)
		assertTrue( json.contains("a_Make Appointment"))
	}

	@Test
	void test_listTestObjectId_byString() {
		String pattern = "button_"
		String json = instance.listTestObjectId(pattern, false)
		println '********** test_listTestObjectId_byString **********'
		println json
		List list = instance.listTestObjectIdRaw(pattern, true)
		assertTrue( list.size() > 0 )
		assertTrue(json.contains("button_Login"))
	}

	@Test
	void test_listTestObjectId_byRegex() {
		String pattern = "button_(\\w+)"
		String json = instance.listTestObjectId(pattern, true)
		println '********** test_listTestObjectId_byRegex **********'
		println json
		//
		List list = instance.listTestObjectIdRaw(pattern, true)
		assertTrue( list.size() > 0 )
		assertTrue(json.contains("button_Login"))
	}

	@Test
	void test_listEssence_default() {
		String pattern = ""
		Boolean isRegex = false
		String json = instance.listEssence(pattern, isRegex)
		println "********** test_listEssence_default *********"
		println json
		List<Map<String, String>> result =
				instance.listEssenceRaw(pattern, isRegex)
		assertTrue( result.size() > 0 )
		assertTrue("json should contain 'a_Make Appointment'", json.contains("a_Make Appointment"))
	}

	@Test
	void test_listEssencetRaw_arg_string() {
		String pattern = "button_"
		Boolean isRegex = false
		String json = instance.listEssenceRaw(pattern, isRegex)
		println "********** test_listEssenceRaw_arg_string *********"
		println json
		List<Map<String, String>> result =
				instance.listEssenceRaw(pattern, isRegex)
		assertTrue( result.size() > 0 )
		assertTrue(json.contains("button_Login"))
	}

	@Test
	void test_getAllTestObjectIds() {
		Set<TestObjectId> allTOI = instance.getAllTestObjectIds()
		println "********** test_getAllTestObjectIds **********"
		allTOI.forEach({ toi ->
			println toi
			assertNotNull(toi.value())
			assertNotEquals("", toi.value())
		})
	}

	@Test
	void test_reverseLookupRaw() {
		Map<Locator, Set<TestObjectEssence>> result = instance.reverseLookupRaw()
		// TODO
	}

	@Test
	void test_reverseLookup() {
		String json = instance.reverseLookup()
		println "********** test_reverseLookup **********"
		println json
	}
}
