package com.kazurayam.ks.testobject.gc


import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testobject.TestObjectId

import groovy.json.JsonOutput
import internal.GlobalVariable

@RunWith(JUnit4.class)
public class GarbagesTest {
	
	private Garbages garbages
	
	@Before
	void setup() {
		garbages = new Garbages()
		ForwardReference reference = ForwardReferenceTest.createSampleInstance()
		TestObjectId testObjectId = reference.getTestObjectEssence().getTestObjectId()
		garbages.add(testObjectId)
	}
	
	@Test
	void test_size() {
		assertEquals(1, garbages.size())	
	}
	
	@Test
	void test_getAll() {
		assertNotNull(garbages.getAllTestObjectIds())
		assertEquals(1, garbages.getAllTestObjectIds().size())
	}
	
	@Test
	void test_toJson() {
		String json = garbages.toJson()
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName("test_toJson.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue(json.contains("Page_CURA Healthcare Service/a_Make Appointment"))
	}
}
