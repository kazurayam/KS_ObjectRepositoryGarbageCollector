package com.kazurayam.ks.testobject.combine

import com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver
import com.kazurayam.ks.configuration.RunConfigurationConfigurator
import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.Locator
import com.kazurayam.ks.testobject.SelectorMethod
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import java.nio.file.Path

import static org.junit.Assert.*

class CombinedLocatorIndexTest {

    private static Path projectDir = KatalonProjectDirectoryResolver.getProjectDir()
    private static Path objectRepositoryDir = projectDir.resolve("Object Repository")
    private static Path scriptsDir = projectDir.resolve("Scripts")

    private static ObjectRepositoryGarbageCollector orgc

    @BeforeClass
    static void beforeClass() {
        RunConfigurationConfigurator.configureProjectDir()

        orgc = new ObjectRepositoryGarbageCollector.Builder(objectRepositoryDir, scriptsDir)
                .includeScriptsFolder("main")
                .includeScriptsFolder("misc")
                .includeObjectRepositoryFolder("main")
                .includeObjectRepositoryFolder("misc")
                .build()
    }

    @Before
    void setup() {}

    @Test
    void test_size() {
        CombinedLocatorIndex clx = orgc.getCombinedLocatorIndex()
        assertTrue(clx.size() > 0)
    }

    @Test
    void test_toJson() {
        CombinedLocatorIndex clx = orgc.getCombinedLocatorIndex()
        String json = clx.toJson()
        Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
                .fileName("test_toJson.json").build()
        sh.write(JsonOutput.prettyPrint(json))
        assertNotNull(json)
        assertTrue(json.contains("a_Make Appointment"))
    }

    @Test
    void test_suspect() {
        CombinedLocatorIndex clx = orgc.getCombinedLocatorIndex()
        String json = clx.suspect()
        //
        Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
                .fileName("test_suspect.json").build()
        sh.write(JsonOutput.prettyPrint(json))
        assertNotNull(json)
        assertTrue(json.contains("//body"))
    }

    @Test
    void test_remove() {
        CombinedLocatorIndex clx = orgc.getCombinedLocatorIndex()
        int previousSize = clx.size()
        Locator key = new Locator("//body", SelectorMethod.XPATH)
        CombinedLocatorDeclarations value = clx.remove(key)
        if (value != null) {
            assertEquals(previousSize - 1, clx.size())
        } else {
            assertEquals(previousSize, clx.size())
        }
    }
}
