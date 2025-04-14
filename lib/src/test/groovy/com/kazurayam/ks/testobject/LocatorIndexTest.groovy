package com.kazurayam.ks.testobject

import com.kazurayam.ks.reporting.Shorthand
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

@RunWith(JUnit4.class)
class LocatorIndexTest {

	private ObjectRepositoryDecorator xor
	private LocatorIndex locatorIndex

	@Before
	void setup() {
		xor = new ObjectRepositoryDecorator.Builder().build()
		locatorIndex = new LocatorIndex()
		List<TestObjectId> toiList = xor.getTestObjectIdList()
		toiList.each { toi ->
			TestObjectEssence essence = toi.toTestObjectEssence()
			Locator locator = essence.getLocator()
			locatorIndex.put(locator, essence)
		}
	}

	@Test
	void test_size() {
		assertTrue(locatorIndex.size() > 0)
	}

	@Test
	void test_toJson() {
		String json = locatorIndex.toJson()
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_toJson.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertNotNull(json)
		assertTrue(json.contains("a_Make Appointment"))
	}
}
