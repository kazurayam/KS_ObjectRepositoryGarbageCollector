package com.kazurayam.ks.testobject

import java.nio.file.Path
import java.nio.file.Paths

import org.junit.BeforeClass
import org.junit.FixMethodOrder;
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters;


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
	void test_dryrun() {
		gc.dryrun()
	}
}
