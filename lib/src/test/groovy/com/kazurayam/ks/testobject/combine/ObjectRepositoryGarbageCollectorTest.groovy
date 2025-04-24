package com.kazurayam.ks.testobject.combine

import com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver

import static org.junit.Assert.*

import java.nio.file.Path

import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters
import com.kazurayam.ks.testobject.TestObjectId
import com.kazurayam.ks.reporting.Shorthand

import groovy.json.JsonOutput

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)
public class ObjectRepositoryGarbageCollectorTest {

	private static Path projectDir = KatalonProjectDirectoryResolver .getProjectDir()
	private static Path objectRepositoryDir = projectDir.resolve("Object Repository")
	private static Path scriptsDir = projectDir.resolve("Scripts")

	private static ObjectRepositoryGarbageCollector garbageCollector

	@BeforeClass
	static void beforeClass() {
		garbageCollector = new ObjectRepositoryGarbageCollector.Builder(objectRepositoryDir, scriptsDir)
				.includeScriptsFolder("main")
				.includeScriptsFolder("misc")
				.includeObjectRepositoryFolder("main")
				.includeObjectRepositoryFolder("misc")
				.build()
	}

	@Test
	void test_db() {
		Database db = garbageCollector.db()
		assertNotNull(db)
		assertNotEquals(0, db.size())
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_db.json").build()
		sh.write(JsonOutput.prettyPrint(db.toJson()))
	}

	@Test
	void test_createBackwardReferencesMap() {
		BackwardReferencesMap brm = garbageCollector.createBackwardReferencesMap()
		assertNotNull(brm)
		TestObjectId toi = new TestObjectId("main/Page_CURA Healthcare Service/a_Go to Homepage")
		assertTrue(brm.keySet().contains(toi))
		Set<ForwardReference> refs = brm.get(toi)
		List<ForwardReference> refList = refs as List
		assertEquals(1, refList.size())
		assertEquals(toi, refList.get(0).getTestObjectEssence().getTestObjectId())
	}

	@Test
	void test_jsonifyBackwardReferencesMap() {
		String json = garbageCollector.jsonifyBackwardReferencesMap()
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_jsonifyBackwardReferencesMap.json").build()
		sh.write(JsonOutput.prettyPrint(json))
	}

	@Test
	void test_getLocatorIndex_with_pattern() {
		LocatorIndex locatorIndex =
				garbageCollector.getLocatorIndex("td[31]", false)
		assertNotNull(locatorIndex)
		assertEquals(1, locatorIndex.size())
	}

	@Test
	void test_jsonifyLocatorIndex() {
		String json = garbageCollector.jsonifyLocatorIndex()
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_jsonifyLocatorIndex.json").build()
		sh.write(JsonOutput.prettyPrint(json))
	}

	@Test
	void test_jsonifyLocatorIndex_with_pattern() {
		String json = garbageCollector.jsonifyLocatorIndex("td[31]", false)
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_jsonifyLocatorIndex_with_pattern.json").build()
		sh.write(JsonOutput.prettyPrint(json))
	}

	@Test
	void test_getGarbage() {
		Garbage garbage = garbageCollector.getGarbage()
		assertNotNull("the garbage variable is null", garbage)
		assertTrue("the Garbage object is empty", garbage.size() > 0)
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_getGarbage.json").build()
		sh.write(JsonOutput.prettyPrint(garbage.toJson()))
		//assertTrue(garbage.contains(new TestObjectId("Page_CURA Healthcare Service/a_Foo")))
	}

	@Test
	void test_jsonifyGarbage() {
		String json = garbageCollector.jsonifyGarbage()
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_jsonifyGarbage.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue("json should contain 'a_Foo'", json.contains("a_Foo"))
	}

	@Test
	void test_jsonifyDatabase() {
		String json = garbageCollector.jsonifyDatabase()
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_jsonifyDatabase.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue("json should contain `//a[@id='btn-make-appointment']`", json.contains("//a[@id='btn-make-appointment']"))
	}
}
