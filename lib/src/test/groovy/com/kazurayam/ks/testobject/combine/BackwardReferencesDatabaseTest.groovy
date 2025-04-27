package com.kazurayam.ks.testobject.combine

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
class BackwardReferencesDatabaseTest {

    private static Path projectDir = KatalonProjectDirectoryResolver.getProjectDir()
    private static Path objectRepositoryDir = projectDir.resolve("Object Repository")
    private static Path scriptsDir = projectDir.resolve("Scripts")

    private static ObjectRepositoryGarbageCollector garbageCollector

    private BackwardReferencesDatabase backwardReferenceMap

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
        backwardReferenceMap = garbageCollector.getBackwardReferencesDatabase()
        assertNotNull(backwardReferenceMap)
    }

    @Test
    void test_toJson() {
        String json = backwardReferenceMap.toJson()
        Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
                .fileName("test_toJson.json").build()
        sh.write(JsonOutput.prettyPrint(json))
    }
}
