package com.kazurayam.ks.testobject

import static org.junit.Assert.*

import java.nio.file.Path
import java.nio.file.Paths

import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kazurayam.ks.testobject.ObjectRepositoryExtension
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject

@RunWith(JUnit4.class)
class ObjectRepositoryExtensionTest {

	private static final Path objectRepository = Paths.get("./Object Repository")

	@BeforeClass
	static void beforeClass() {
		ObjectRepositoryExtension.apply()
	}

	@Test
	void testList() {
		List<String> list = ObjectRepository.list()
		println "********** testList() **********"
		list.forEach { p ->
			println p
		}
		assertTrue(list.size() > 0)
	}

	@Test
	void testList_args() {
		List<String> list = ObjectRepository.list("button_", false)
		println "********** testList_args() **********"
		list.forEach { p ->
			println p
		}
		assertTrue(list.size() > 0)
	}

	@Test
	void testListWithLocatorRaw() {
		List<Map<String, String>> result = ObjectRepository.listWithLocatorRaw()
		println "********** testListWithLocatorRaw *********"
		result.forEach { m ->
			println m
		}
	}

	@Test
	void testListWithLocatorRaw_args() {
		List<Map<String, String>> result = ObjectRepositoryExtension.listWithLocatorRaw("button_")
		println "********** testListWithLocatorRaw_args *********"
		result.forEach { m ->
			println m
		}
	}

	@Test
	void testListWithLocator() {
		String json = ObjectRepository.listWithLocator()
		println "********** testListWithLocator *******"
		println json
	}

	@Test
	void testListWithLocator_args() {
		String json = ObjectRepository.listWithLocator("button_")
		println "********** testListWithLocator_args *******"
		println json
	}

	@Test
	void testReverseLookupRaw() {
		Map<String, Set<String>> result = ObjectRepository.reverseLookupRaw()
		assertEquals(12, result.size())
	}

	@Test
	void testReverseLookupRaw_args() {
		String pattern = "btn-(.+)-appointment"
		Map<String, Set<String>> result = ObjectRepository.reverseLookupRaw(pattern, true)
		println "********** testRevLookupRaw_args() **********"
		println "pattern: ${pattern}"
		result.forEach { k, v ->
			println k + "\n\t" + v.toString()
		}
		assertEquals(2, result.size())
		//
		pattern = "select"
		result = ObjectRepository.reverseLookupRaw("select", false)
		println "pattern: ${pattern}"
		result.forEach { k, v ->
			println k + "\n\t" + v.toString()
		}
		assertEquals(1, result.size())
	}

	@Test
	void testReverseLookup() {
		String json = ObjectRepository.reverseLookup()
		println "********** testRevLookupAsJson() **********"
		println json
		assertNotNull(json)
	}

	@Test
	void testReverseLookup_args() {
		String json = ObjectRepository.reverseLookup("//a[@id")   // default: isRegex = false
		println "********** testRevLookup_args() **********"
		println json
		assertNotNull(json)
	}

	/**
	 * test if ObjectRepository's ordinary method works
	 */
	@Test
	void testFindTestObject() {
		TestObject tObj = ObjectRepository.findTestObject("Page_CURA Healthcare Service/button_Login")
		assertNotNull(tObj)
	}
}