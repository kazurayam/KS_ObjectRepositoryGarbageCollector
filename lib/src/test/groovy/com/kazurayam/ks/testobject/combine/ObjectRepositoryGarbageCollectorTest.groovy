package com.kazurayam.ks.testobject.combine

import com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver
import com.kazurayam.ks.configuration.RunConfigurationConfigurator
import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.TestObjectId
import groovy.json.JsonOutput
import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters

import java.nio.file.Path
import java.nio.file.Paths

import static org.junit.Assert.*

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)
class ObjectRepositoryGarbageCollectorTest {

	private static Path projectDir = KatalonProjectDirectoryResolver.getProjectDir()
	private static Path objectRepositoryDir = projectDir.resolve("Object Repository")
	private static Path scriptsDir = projectDir.resolve("Scripts")

	private static ObjectRepositoryGarbageCollector garbageCollector

	@BeforeClass
	static void beforeClass() {
		RunConfigurationConfigurator.configureProjectDir()

		garbageCollector = new ObjectRepositoryGarbageCollector.Builder(objectRepositoryDir, scriptsDir)
				.includeScriptsFolder("main")
				.includeScriptsFolder("misc")
				.includeObjectRepositoryFolder("main")
				.includeObjectRepositoryFolder("misc")
				.build()
	}

	@Test
	void test_getForwardReferences() {
		ForwardReferences forwardReferences = garbageCollector.getForwardReferences()
		assertNotNull(forwardReferences)
		assertNotEquals(0, forwardReferences.size())
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_getForwardReferences.json").build()
		sh.write(JsonOutput.prettyPrint(forwardReferences.toJson()))
	}

	@Test
	void test_getBackwardReferenceIndex() {
		BackwardReferenceIndex brm = garbageCollector.getBackwardReferenceIndex()
		assertNotNull(brm)
		Path relativePath = Paths.get("main/Page_CURA Healthcare Service/a_Go to Homepage.rs")
		TestObjectId toi = new TestObjectId(relativePath)
		assertTrue(brm.keySet().contains(toi))
		Set<BackwardReference> brSet = brm.get(toi)
		List<BackwardReference> brList = brSet as List
		assertEquals(1, brList.size())
		assertEquals(toi, brList.get(0).getTestObjectId())
	}

	@Test
	void test_jsonifyBackwardReferenceIndex() {
		String json = garbageCollector.jsonifyBackwardReferenceIndex()
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_jsonifyBackwardReferenceIndex.json").build()
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
	void test_jsonifyForwardReferences() {
		String json = garbageCollector.jsonifyForwardReferences()
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_jsonifyDatabase.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		String expected = "main/Page_CURA Healthcare Service/a_Make Appointment"
		assertTrue("json should contain `${expected}`",
				json.contains(expected))
	}

	/**
	 * The test_jsonifyGarbage() reported that there are 5 unused Test Objects.
	 * the CombinedLocatorIndex and the SuspiciousLocatorIndex must contain
	 * these entities as well.
	 */
	private GARBAGE_LIST = [
			"main/Page_CURA Healthcare Service/a_Foo",
			"main/Page_CURA Healthcare Service/td_28",
			"main/Page_CURA Healthcare Service/xtra/a_Go to Homepage",
			"main/Page_CURA Healthcare Service/xtra/td_28",
			"misc/dummy1"
	]

	@Test
	void test_jsonifyCombinedLocatorIndex() {
		String json = garbageCollector.jsonifyCombinedLocatorIndex()
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_jsonifyCombinedLocatorIndex.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		GARBAGE_LIST.each { testObjectId ->
			assertTrue("json should contain `${testObjectId}`", json.contains(testObjectId))
		}
	}

	@Test
	void test_jsonifySuspiciousLocatorIndex() {
		String json = garbageCollector.jsonifySuspiciousLocatorIndex()
		Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
				.fileName("test_jsonifySuspiciousLocatorIndex.json").build()
		sh.write(JsonOutput.prettyPrint(json))
		GARBAGE_LIST.each { testObjectId ->
			assertTrue("json should contain `${testObjectId}`", json.contains(testObjectId))
		}
	}
}