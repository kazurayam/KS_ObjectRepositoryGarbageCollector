package com.kazurayam.ks.testobject.combine

import static org.junit.Assert.*

import com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver
import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.TestObjectEssence
import com.kazurayam.ks.testobject.TestObjectId
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import java.nio.file.Path

class BackwardReferencesTest {

    private static Path projectDir = KatalonProjectDirectoryResolver .getProjectDir()
    private static Path objectRepositoryDir = projectDir.resolve("Object Repository")
    private static Path scriptsDir = projectDir.resolve("Scripts")

    private static ObjectRepositoryGarbageCollector garbageCollector
    private static BackwardReferencesMap backwardReferenceMap

    private BackwardReferences backwardReference

    @BeforeClass
    static void beforeClass() {
        garbageCollector = new ObjectRepositoryGarbageCollector.Builder(objectRepositoryDir, scriptsDir)
                .includeScriptsFolder("main")
                .includeScriptsFolder("misc")
                .includeObjectRepositoryFolder("main")
                .includeObjectRepositoryFolder("misc")
                .build()
        backwardReferenceMap = garbageCollector.createBackwardReferencesMap()
    }

    @Before
    void setup() {
        TestObjectId testObjectId = new TestObjectId("main/Page_CURA Healthcare Service/a_Go to Homepage")
        TestObjectEssence testObjectEssence = garbageCollector.getTestObjectEssence(testObjectId)
        Set<ForwardReference> forwardReferences = backwardReferenceMap.get(testObjectId)
        backwardReference = new BackwardReferences(testObjectEssence, forwardReferences)
    }

    @Test
    void test_toJson() {
        String json = backwardReference.toJson()
        Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
                .fileName("test_toJson.json").build()
        sh.write(JsonOutput.prettyPrint(json))
        assertNotNull(json)
        assertTrue(json.contains("a_Go to Homepage"))
    }
}
