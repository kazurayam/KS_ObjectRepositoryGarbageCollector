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
	void testListWithLocator() {
		List<Map<String, String>> result = ObjectRepository.listWithLocator()
		println "********** testListWithLocator *********"
		result.forEach { m ->
			println m
		}
	}
	
	@Test
	void testListWithLocator_args() {
		List<Map<String, String>> result = ObjectRepositoryExtension.listWithLocator("button_")
		println "********** testListWithLocator_args *********"
		result.forEach { m ->
			println m
		}
	}
	
	@Test
	void testListWithLocatorAsString() {
		String json = ObjectRepository.listWithLocatorAsJson()
		println "********** testListWithLocatorAsString *******"
		println json
	}
	
	@Test
	void testListWithLocatorAsString_args() {
		String json = ObjectRepository.listWithLocatorAsJson("button_")
		println "********** testListWithLocatorAsString_args *******"
		println json
	}
	
	@Test
	void testReverseLookup() {
		Map<String, Set<String>> result = ObjectRepository.reverseLookup()
		assertEquals(12, result.size())
	}

	@Test
	void testReverseLookup_args() {
		String pattern = "btn-(.+)-appointment"
		Map<String, Set<String>> result = ObjectRepository.reverseLookup(pattern, true)
		println "********** testRevLookup_args() **********"
		println "pattern: ${pattern}"
		result.forEach { k, v ->
			println k + "\n\t" + v.toString()
		}
		assertEquals(2, result.size())
		//
		pattern = "select"
		result = ObjectRepository.reverseLookup("select", false)
		println "pattern: ${pattern}"
		result.forEach { k, v ->
			println k + "\n\t" + v.toString()
		}
		assertEquals(1, result.size())
	}

	@Test
	void testReverseLookupAsJson() {
		String json = ObjectRepository.reverseLookupAsJson()
		println "********** testRevLookupAsJson() **********"
		println json
		assertNotNull(json)
	}

	@Test
	void testReverseLookupAsJson_args() {
		String json = ObjectRepository.reverseLookupAsJson("//a[@id")   // default: isRegex = false
		println "********** testRevLookupAsJson_args() **********"
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