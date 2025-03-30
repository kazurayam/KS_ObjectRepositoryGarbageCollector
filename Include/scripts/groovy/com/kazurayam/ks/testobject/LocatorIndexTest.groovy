package com.kazurayam.ks.testobject

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import com.kazurayam.ks.testobject.ExtendedObjectRepository
import com.kazurayam.ks.reporting.Shorthand

import internal.GlobalVariable

@RunWith(JUnit4.class)
public class LocatorIndexTest {

	private ExtendedObjectRepository eor
	private LocatorIndex locatorIndex

	@Before
	public void setup() {
		eor = new ExtendedObjectRepository()
		locatorIndex = new LocatorIndex()
		List<TestObjectId> toiList = eor.getTestObjectIdList()
		toiList.each { toi ->
			TestObjectEssence essence = toi.toTestObjectEssence()
			Locator locator = essence.locator()
			locatorIndex.put(locator, essence)
		}
	}

	@Test
	public void test_size() {
		assertTrue(locatorIndex.size() > 0)
	}
	
	@Test
	public void test_toJson() {
		String json = locatorIndex.toJson()
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
						.fileName("test_toJson.json").build()
		sh.write(json)
	}
}
