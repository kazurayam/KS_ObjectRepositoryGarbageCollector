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


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)

public class ObjectRepositoryGCTest {

	private static ObjectRepositoryGC gc

	@BeforeClass
	static void beforeClass() {
		Path projectDir = Paths.get(".")
		Path objectRepositoryDir = projectDir.resolve("Object Repository")
		Path scriptsDir = projectDir.resolve("Scripts")
		gc = new ObjectRepositoryGC(objectRepositoryDir, scriptsDir)
	}

	@Test
	void test_db() {
		Database db = gc.db()
		assertNotNull(db)
	}

	@Test
	void test_resolveRaw() {
		Map<TestObjectId, Set<TCTOReference>> resolution = gc.resolveRaw()
		assertNotNull(resolution)
	}

	@Test
	void test_resolve() {
		String json = gc.resolve()
		println json
	}
	
	@Test
	void test_garbagesRaw() {
		List<TestObjectId> garbages = gc.garbagesRaw()
		assertNotNull(garbages)
	}
	
	@Test
	void test_garbages() {
		String json = gc.garbages()
		println json
	}
}
