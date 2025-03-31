package com.kazurayam.ks.testobject.gc

import static org.junit.Assert.*

import java.nio.file.Path
import java.nio.file.Paths

import org.junit.BeforeClass
import org.junit.FixMethodOrder;
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters;

import com.kazurayam.ks.testobject.TestObjectId

import groovy.json.JsonOutput

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)
public class ObjectRepositoryGarbageCollectorTest {

	private static Path projectDir = Paths.get(".")
	private static Path objectRepositoryDir = projectDir.resolve("Object Repository")
	private static Path scriptsDir = projectDir.resolve("Scripts")

	private static ObjectRepositoryGarbageCollector gc

	@BeforeClass
	static void beforeClass() {
		gc = new ObjectRepositoryGarbageCollector.Builder(objectRepositoryDir, scriptsDir).build()
	}

	@Test
	void test_db() {
		Database db = gc.db()
		assertNotNull(db)
		println "********** test_db **********"
		println JsonOutput.prettyPrint(db.toJson())
		assertNotEquals(0, db.size())
	}

	@Test
	void test_resolveRaw() {
		Map<TestObjectId, Set<ForwardReference>> resolved = gc.resolveRaw()
		assertNotNull(resolved)
		TestObjectId toi = new TestObjectId("Page_CURA Healthcare Service/a_Go to Homepage")
		assertTrue(resolved.keySet().contains(toi))
		Set<ForwardReference> refs = resolved.get(toi)
		List<ForwardReference> refList = refs as List
		assertEquals(1, refList.size())
		assertEquals(toi, refList.get(0).testObjectEssence().testObjectId())
	}

	@Test
	void test_resolve() {
		String json = gc.resolve()
		println "********** test_resolve **********"
		println json
	}

	@Test
	void test_garbagesRaw() {
		List<TestObjectId> garbages = gc.garbagesRaw()
		println "********** test_garbagesRaw **********"
		garbages.forEach { toi ->
			println toi.toString()
		}
		assertNotNull(garbages)
		assertTrue(garbages.contains(new TestObjectId("Page_CURA Healthcare Service/a_Foo")))
	}

	@Test
	void test_garbages() {
		String json = gc.garbages()
		println "********** test_garbages **********"
		println json
		assertTrue("json should contain 'a_Foo'", json.contains("a_Foo"))
	}

	@Test
	void test_Builder_objrepoSubpath() {
		ObjectRepositoryGarbageCollector gc = new ObjectRepositoryGarbageCollector.Builder(objectRepositoryDir, scriptsDir)
				.objectRepositorySubpath("Page_CURA Healthcare Service")
				.build()
	}

	@Test
	void test_Builder_scriptsSubpath() {
		ObjectRepositoryGarbageCollector gc = new ObjectRepositoryGarbageCollector.Builder(objectRepositoryDir, scriptsDir)
				.testCasesSubpath("main")
				.build()
	}
}
