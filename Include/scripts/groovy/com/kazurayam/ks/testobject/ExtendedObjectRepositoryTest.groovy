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
	void test_list() {
		String json = instance.list("", false)
		println '********** test_list **********'
		println json
	}
	
	@Test
	void test_list_arg_string() {
		String json = instance.list("button_", false)
		println '********** test_list_arg_string **********'
		println json
	}
	
	@Test
	void test_list_arg_regex() {
		String pattern = "btn-(.+)-appointment"
		String json = instance.list(pattern, true)
		println '********** test_list_arg_regex **********'
		println json
	}
	
	@Test
	void test_listRaw_default() {
		List<String> list = instance.listRaw("", false)
		assertTrue( list.size() > 0 )
	}
	
	@Test
	void test_listRaw_arg_string() {
		List<String> list = instance.listRaw("button_", false)
		assertTrue( list.size() > 0 )
	}
	
	@Test
	void test_listRaw_arg_regex() {
		String pattern = "btn-(.+)-appointment"
		List<String> list = instance.listRaw(pattern, true)
		assertTrue( list.size() > 0 )
	}
	
}
