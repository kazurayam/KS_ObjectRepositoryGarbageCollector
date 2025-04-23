package com.kazurayam.ks.testobject.gc

import com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver
import com.kazurayam.ks.reporting.Shorthand
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters

import java.nio.file.Path

import static org.junit.Assert.assertNotNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)
class BackwardReferencesTest {

    private static Path projectDir = KatalonProjectDirectoryResolver.getProjectDir()
    private static Path objectRepositoryDir = projectDir.resolve("Object Repository")
    private static Path scriptsDir = projectDir.resolve("Scripts")

    private static ObjectRepositoryGarbageCollector garbageCollector

    private BackwardReferences backwardReferences

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
        backwardReferences = garbageCollector.getBackwardReferences()
        assertNotNull(backwardReferences)
    }

    @Test
    void test_toJson() {
        String json = backwardReferences.toJson()
        Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
                .fileName("test_toJson.json").build()
        sh.write(JsonOutput.prettyPrint(json))
    }
}
