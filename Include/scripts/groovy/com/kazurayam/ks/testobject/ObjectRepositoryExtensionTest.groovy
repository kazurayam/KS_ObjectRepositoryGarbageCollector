package com.kazurayam.ks.testobject

import static org.junit.Assert.*

import java.nio.file.Path
import java.nio.file.Paths

import org.junit.BeforeClass
import org.junit.FixMethodOrder;
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters;

import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)
class ObjectRepositoryExtensionTest {

	private static final Path objectRepository = Paths.get("./Object Repository")

	@BeforeClass
	static void beforeClass() {
		ObjectRepositoryExtension ext = new ObjectRepositoryExtension()
		ext.apply()
	}

	@Test
	void test_listRaw() {
		List<String> list = ObjectRepository.listRaw()
		println "********** test_listRaw() **********"
		list.forEach { p ->
			println p
		}
		assertTrue(list.size() > 0)
	}

	@Test
	void test_listRaw_args() {
		List<String> list = ObjectRepository.listRaw("button_", false)
		println "********** test_listRaw_args() **********"
		list.forEach { p ->
			println p
		}
		assertTrue(list.size() > 0)
	}

	@Test
	void test_list() {
		String json = ObjectRepository.list()
		println "********** test_list **********"
		println json
	}

	@Test
	void test_list_args() {
		String json = ObjectRepository.list("button_")
		println "********** test_list_args **********"
		println json
	}

	@Test
	void test_listGistRaw() {
		List<TestObjectGist> result = ObjectRepository.listGistRaw()
		println "********** test_listTestObjectGistRaw *********"
		result.forEach { m ->
			println m
		}
	}

	@Test
	void test_listGistRaw_args() {
		List<TestObjectGist> result = ObjectRepository.listGistRaw("button_")
		println "********** test_listGistRaw_args *********"
		result.forEach { m ->
			println m
		}
	}

	@Test
	void test_listGist() {
		String json = ObjectRepository.listGist()
		println "********** test_listGiest *******"
		println json
	}

	@Test
	void test_listGist_args() {
		String json = ObjectRepository.listGist("button_")
		println "********** test_listGist_args *******"
		println json
	}

	@Test
	void test_reverseLookupRaw() {
		Map<Locator, Set<TestObjectGist>> result = ObjectRepository.reverseLookupRaw()
		assertEquals(12, result.size())
	}

	@Test
	void test_reverseLookupRaw_args() {
		String pattern = "btn-(.+)-appointment"
		Map<Locator, Set<TestObjectGist>> result = ObjectRepository.reverseLookupRaw(pattern, true)
		println "********** test_reverseLookupRaw_args() **********"
		println "pattern: ${pattern}"
		result.forEach { k, v ->
			println k.toString() + "\n\t" + v.toString()
		}
		assertEquals(2, result.size())
		//
		pattern = "select"
		result = ObjectRepository.reverseLookupRaw("select", false)
		println "pattern: ${pattern}"
		result.forEach { k, v ->
			println k.toString() + "\n\t" + v.toString()
		}
		assertEquals(1, result.size())
	}

	@Test
	void test_reverseLookup() {
		String json = ObjectRepository.reverseLookup()
		println "********** test_reverseLookup() **********"
		println json
		assertNotNull(json)
	}

	@Test
	void test_reverseLookup_args() {
		String json = ObjectRepository.reverseLookup("//a[@id")   // default: isRegex = false
		println "********** test_reverseLookup_args() **********"
		println json
		assertNotNull(json)
	}

	/**
	 * test if ObjectRepository's ordinary method works
	 */
	@Test
	void test_findTestObject() {
		TestObject tObj = ObjectRepository.findTestObject("Page_CURA Healthcare Service/button_Login")
		assertNotNull(tObj)
	}
}