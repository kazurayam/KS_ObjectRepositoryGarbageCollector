package com.kazurayam.ks.testobject

import static org.junit.Assert.*

import org.junit.Before
import org.junit.FixMethodOrder;
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters;

import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files

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
	void test_list_default() {
		String json = instance.list("", false)
		println '********** test_list_default **********'
		println json
		//
		List list = instance.listRaw("", false)
		assertTrue( list.size() > 0 )
	}

	@Test
	void test_list_byString() {
		String pattern = "button_"
		String json = instance.list(pattern, false)
		println '********** test_list_byString **********'
		println json
		List list = instance.listRaw(pattern, true)
		assertTrue( list.size() > 0 )
	}

	@Test
	void test_list_byRegex() {
		String pattern = "button_(\\w+)"
		String json = instance.list(pattern, true)
		println '********** test_list_byRegex **********'
		println json
		//
		List list = instance.listRaw(pattern, true)
		assertTrue( list.size() > 0 )
	}

	@Test
	void test_listWithLocator_default() {
		String pattern = ""
		Boolean isRegex = false
		String json = instance.listWithLocator(pattern, isRegex)
		println "********** test_listWithLocator_default *********"
		println json
		List<Map<String, String>> result =
				instance.listWithLocatorRaw(pattern, isRegex)
		assertTrue( result.size() > 0 )
	}

	@Test
	void test_listWithLocator_arg_string() {
		String pattern = "button_"
		Boolean isRegex = false
		String json = instance.listWithLocatorRaw(pattern, isRegex)
		println "********** test_listWithLocatorRaw_arg *********"
		println json
		List<Map<String, String>> result =
				instance.listWithLocatorRaw(pattern, isRegex)
		assertTrue( result.size() > 0 )
	}

}
