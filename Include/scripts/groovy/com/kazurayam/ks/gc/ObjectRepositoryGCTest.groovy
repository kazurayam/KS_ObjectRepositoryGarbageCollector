package com.kazurayam.ks.gc

import java.nio.file.Path
import java.nio.file.Paths

import static org.junit.Assert.*
import org.junit.BeforeClass
import org.junit.FixMethodOrder;
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters;

import com.kazurayam.ks.gc.ObjectRepositoryGC
import com.kazurayam.ks.testobject.TestObjectId
import com.kazurayam.ks.gc.TCTOReference

import groovy.json.JsonOutput
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.burt.jmespath.Expression
import io.burt.jmespath.JmesPath
import io.burt.jmespath.jackson.JacksonRuntime

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)
public class ObjectRepositoryGCTest {

	private static ObjectRepositoryGC gc

	private static JmesPath<JsonNode> jmespath
	private static ObjectMapper objectMapper

	@BeforeClass
	static void beforeClass() {
		Path projectDir = Paths.get(".")
		Path objectRepositoryDir = projectDir.resolve("Object Repository")
		Path scriptsDir = projectDir.resolve("Scripts")
		gc = new ObjectRepositoryGC.Builder(objectRepositoryDir, scriptsDir).build()
		//
		jmespath = new JacksonRuntime()
		objectMapper = new ObjectMapper()
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
		Map<TestObjectId, Set<TCTOReference>> resolved = gc.resolveRaw()
		assertNotNull(resolved)
		TestObjectId toi = new TestObjectId("Page_CURA Healthcare Service/a_Go to Homepage")
		assertTrue(resolved.keySet().contains(toi))
		Set<TCTOReference> refs = resolved.get(toi)
		List<TCTOReference> refList = refs as List
		assertEquals(1, refList.size())
		assertEquals(toi, refList.get(0).testObjectGist().testObjectId())
	}

	@Test
	void test_resolve() {
		String json = gc.resolve(true)
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
}
