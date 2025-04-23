package com.kazurayam.ks.testobject

import com.kazurayam.ks.reporting.Shorthand
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertEquals

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

	/**
	 * remove one entry Locator like
	 * <code>
	 *     {
	 *     "Locator": "//body",
	 *     "Number of duplicates": 1,
	 *     "TestObjectEssences": [
	 *     {
	 *     "TestObjectId": "misc/dummy1",
	 *     "Number of referrers": 0,
	 *     "Method": "XPATH",
	 *     "Locator": "//body"
	 *     }
	 *     ]
	 *     },
	 *     </code>
	 * then check the size of the LocatorIndex to be decremented
	 */
	@Test
	void test_remove() {
		int previousSize = locatorIndex.size()
		Locator key = new Locator("//body")
		Set<TestObjectEssence> value = locatorIndex.remove(key)
		assertEquals(previousSize - 1, locatorIndex.size())
	}
}
