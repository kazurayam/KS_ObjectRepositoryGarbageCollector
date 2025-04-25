package com.kazurayam.ks.testobject.combine

import com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver
import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.ObjectRepositoryDecorator
import com.kazurayam.ks.testobject.TestObjectEssence
import com.kazurayam.ks.testobject.TestObjectId
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.nio.file.Path

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertEquals

@RunWith(JUnit4.class)
class LocatorIndexTest {

	private static Path projectDir = KatalonProjectDirectoryResolver .getProjectDir()
	private static Path objectRepositoryDir = projectDir.resolve("Object Repository")
	private static Path scriptsDir = projectDir.resolve("Scripts")

	private static ObjectRepositoryGarbageCollector garbageCollector

	private LocatorIndex locatorIndex

	@BeforeClass
	static void beforeClass() {
		garbageCollector = new ObjectRepositoryGarbageCollector.Builder(objectRepositoryDir, scriptsDir)
				.includeScriptsFolder("main")
				.includeScriptsFolder("misc")
				.includeObjectRepositoryFolder("main")
				.includeObjectRepositoryFolder("misc")
				.build()
	}

	@Before
	void setup() {
		locatorIndex = garbageCollector.getLocatorIndex()
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
		Set<ForwardReference> value = locatorIndex.remove(key)
		if (value != null) {
			assertEquals(previousSize - 1, locatorIndex.size())
		} else {
			assertEquals(previousSize, locatorIndex.size())
		}
	}
}
