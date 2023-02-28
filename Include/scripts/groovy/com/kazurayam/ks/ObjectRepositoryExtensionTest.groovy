package com.kazurayam.ks

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files
import com.kms.katalon.core.testobject.ObjectRepository

@RunWith(JUnit4.class)
class ObjectRepositoryExtensionTest {

	private static final Path objectRepository = Paths.get("./Object Repository")

	@Test
	void testGetTestObjects() {
		ObjectRepositoryExtension.apply()
		//
		List<String> list = ObjectRepository.list()
		assertTrue(list.size() > 0)
		list.forEach { p ->
			println p
		}
	}
}