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
	void testListWithArgs() {
		List<String> list = ObjectRepository.list("button_", false)
		println "********** testListWithArgs() **********"
		list.forEach { p ->
			println p
		}
		assertTrue(list.size() > 0)
	}

	@Test
	void testReverseLookup() {
		Map<String, Set<String>> result = ObjectRepository.reverseLookup()
		assertEquals(12, result.size())
	}

	@Test
	void testReverseLookupWithArgs() {
		String pattern = "btn-(.+)-appointment"
		Map<String, Set<String>> result = ObjectRepository.reverseLookup(pattern, true)
		println "********** testRevLookupWithArgs() **********"
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
	void testReverseLookupAsJsonWithArgs() {
		String json = ObjectRepository.reverseLookupAsJson("//a[@id")   // default: isRegex = false
		println "********** testRevLookupAsJsonWithArg() **********"
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