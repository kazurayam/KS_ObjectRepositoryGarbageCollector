package com.kazurayam.ks.testobject.gc

import static org.junit.Assert.*

import java.nio.file.Path
import java.nio.file.Paths

import org.junit.BeforeClass
import org.junit.FixMethodOrder;
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters;

import com.kazurayam.ks.testobject.TestObjectId
import com.kazurayam.ks.reporting.Shorthand

import groovy.json.JsonOutput
import internal.GlobalVariable

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)
public class ObjectRepositoryGarbageCollectorTest {

	private static Path projectDir = Paths.get(".")
	private static Path objectRepositoryDir = projectDir.resolve("Object Repository")
	private static Path scriptsDir = projectDir.resolve("Scripts")

	private static ObjectRepositoryGarbageCollector garbageCollector

	@BeforeClass
	static void beforeClass() {
		garbageCollector = new ObjectRepositoryGarbageCollector.Builder(objectRepositoryDir, scriptsDir).build()
	}

	@Test
	void test_db() {
		Database db = garbageCollector.db()
		assertNotNull(db)
		assertNotEquals(0, db.size())
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName("test_db.json").build()
		sh.write(JsonOutput.prettyPrint(db.toJson()))
	}

	@Test
	void test_getBackwardReferences() {
		BackwardReferences bRefs = garbageCollector.getBackwardReferences()
		assertNotNull(bRefs)
		TestObjectId toi = new TestObjectId("main/Page_CURA Healthcare Service/a_Go to Homepage")
		assertTrue(bRefs.keySet().contains(toi))
		Set<ForwardReference> refs = bRefs.get(toi)
		List<ForwardReference> refList = refs as List
		assertEquals(1, refList.size())
		assertEquals(toi, refList.get(0).getTestObjectEssence().getTestObjectId())
	}

	@Test
	void test_jsonifyBackwardReferences() {
		String json = garbageCollector.jsonifyBackwardReferences()
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName("test_jsonifyBackwardReferences.json").build()
		sh.write(JsonOutput.prettyPrint(json))
	}

	@Test
	void test_getGarbages() {
		Garbages garbages = garbageCollector.getGarbages()
		assertNotNull("the garbages variable is null", garbages)
		assertTrue("the Garbages object is empty", garbages.size() > 0)
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName("test_getGarbages.json").build()
		sh.write(JsonOutput.prettyPrint(garbages.toJson()))
		//assertTrue(garbages.contains(new TestObjectId("Page_CURA Healthcare Service/a_Foo")))
	}

	@Test
	void test_jsonifyGarbages() {
		String json = garbageCollector.jsonifyGarbages()
		Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName("test_jsonifyGarbages.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		assertTrue("json should contain 'a_Foo'", json.contains("a_Foo"))
	}
}
