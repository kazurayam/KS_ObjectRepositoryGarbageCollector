package com.kazurayam.ks

import static org.junit.Assert.*

import java.nio.file.Path
import java.nio.file.Paths

import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject

@RunWith(JUnit4.class)
class ObjectRepositoryExtensionTest {

	private static final Path objectRepository = Paths.get("./Object Repository")

	@BeforeClass
	static void beforeClass() {
		ObjectRepositoryExtension ext = new ObjectRepositoryExtension()
		ext.apply()
	}
	
	@Test
	void testGetTestObjects() {
		List<String> list = ObjectRepository.list()
		assertTrue(list.size() > 0)
		list.forEach { p ->
			println p
		}
	}
	
	@Test
	void testXrefAsJson() {
		String xref = ObjectRepository.xrefAsJson()
		assertNotNull(xref)
		println xref
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