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
class ObjectRepositoryExtenderTest {

	private static final Path objectRepository = Paths.get("./Object Repository")

	@BeforeClass
	static void beforeClass() {
		ExtendedObjectRepository eor = new ExtendedObjectRepository(objectRepository)
		ObjectRepositoryExtender ext = new ObjectRepositoryExtender(eor)
		ext.apply()
	}

	@Test
	void test_listTestObjectIdRaw() {
		List<String> list = ObjectRepository.listTestObjectIdRaw()
		println "********** test_listTestObjectIdRaw() **********"
		list.forEach { p ->
			println p
		}
		assertTrue(list.size() > 0)
	}

	@Test
	void test_listTestObjectIdRaw_args() {
		List<String> list = ObjectRepository.listTestObjectIdRaw("button_", false)
		println "********** test_listTestObjectIdRaw_args() **********"
		list.forEach { p ->
			println p
		}
		assertTrue(list.size() > 0)
	}

	@Test
	void test_listTestObjectId() {
		String json = ObjectRepository.listTestObjectId()
		println "********** test_listTestObjectId **********"
		println json
	}

	@Test
	void test_listTestObjectId_args() {
		String json = ObjectRepository.listTestObjectId("button_")
		println "********** test_listTestObjectId_args **********"
		println json
	}

	@Test
	void test_listEssenceRaw() {
		List<TestObjectEssence> result = ObjectRepository.listEssenceRaw()
		println "********** test_listEssenceRaw *********"
		result.forEach { m ->
			println m
		}
	}

	@Test
	void test_listEssenceRaw_args() {
		List<TestObjectEssence> result = ObjectRepository.listEssenceRaw("button_")
		println "********** test_listEssenceRaw_args *********"
		result.forEach { m ->
			println m
		}
	}

	@Test
	void test_listEssence() {
		String json = ObjectRepository.listEssence()
		println "********** test_listEssence *******"
		println json
	}

	@Test
	void test_listEssence_args() {
		String json = ObjectRepository.listEssence("button_")
		println "********** test_listEssence_args *******"
		println json
	}

	@Test
	void test_reverseLookupRaw() {
		Map<Locator, Set<TestObjectEssence>> result = ObjectRepository.reverseLookupRaw()
		assertEquals(12, result.size())
	}

	@Test
	void test_reverseLookupRaw_args() {
		String pattern = "btn-(.+)-appointment"
		Map<Locator, Set<TestObjectEssence>> result = ObjectRepository.reverseLookupRaw(pattern, true)
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