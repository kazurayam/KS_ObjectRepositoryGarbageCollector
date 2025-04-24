package com.kazurayam.ks.testobject.combine


import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.TestObjectId

import groovy.json.JsonOutput

@RunWith(JUnit4.class)
public class GarbageTest {
	
	private Garbage garbage
	
	@Before
	void setup() {
		garbage = new Garbage()
		ForwardReference reference = ForwardReferenceTest.createSampleInstance()
		TestObjectId testObjectId = reference.getTestObjectEssence().getTestObjectId()
		garbage.add(testObjectId)
	}
	
	@Test
	void test_size() {
		assertEquals(1, garbage.size())
	}
	
	@Test
	void test_getAll() {
		assertNotNull(garbage.getAllTestObjectIds())
		assertEquals(1, garbage.getAllTestObjectIds().size())
	}
	
	@Test
	void test_toJson() {
		String json = garbage.toJson()
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_toJson.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue(json.contains("Page_CURA Healthcare Service/a_Make Appointment"))
	}
}
