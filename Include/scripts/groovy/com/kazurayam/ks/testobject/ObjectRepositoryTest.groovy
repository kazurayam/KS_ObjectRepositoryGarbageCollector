package com.kazurayam.ks.testobject

import static org.junit.Assert.*

import org.junit.FixMethodOrder;
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters;

import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.configuration.RunConfiguration

/**
 * test if the built-in ObjectRepository class is working --- surely it should
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)
public class ObjectRepositoryTest {

	/**
	 * test if ObjectRepository's ordinary method works
	 */
	@Test
	void test_findTestObject() {
		TestObject tObj = ObjectRepository.findTestObject("Page_CURA Healthcare Service/button_Login")
		assertNotNull(tObj)
	}

	@Test
	void test_RunConfiguration() {
		String projectDir = RunConfiguration.getProjectDir()
		println "projectDir= ${projectDir}"
		assertNotNull(projectDir)
	}
}
