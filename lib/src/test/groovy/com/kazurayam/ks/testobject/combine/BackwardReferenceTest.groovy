package com.kazurayam.ks.testobject.combine

import static org.junit.Assert.*

import com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver
import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.TestObjectId
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import java.nio.file.Path

class BackwardReferenceTest {

    private static Path projectDir = KatalonProjectDirectoryResolver .getProjectDir()
    private static Path objectRepositoryDir = projectDir.resolve("Object Repository")
    private static Path scriptsDir = projectDir.resolve("Scripts")

    private static ObjectRepositoryGarbageCollector garbageCollector
    private static BackwardReferenceIndex index

    private Set<BackwardReference> backwardReferencSet

    @BeforeClass
    static void beforeClass() {
        garbageCollector = new ObjectRepositoryGarbageCollector.Builder(objectRepositoryDir, scriptsDir)
                .includeScriptsFolder("main")
                .includeScriptsFolder("misc")
                .includeObjectRepositoryFolder("main")
                .includeObjectRepositoryFolder("misc")
                .build()
        index = garbageCollector.getBackwardReferenceIndex()
    }

    @Before
    void setup() {
        TestObjectId testObjectId = new TestObjectId("main/Page_CURA Healthcare Service/a_Make Appointment")
        backwardReferencSet = index.get(testObjectId)
    }

    @Test
    void test_toJson() {
        List<BackwardReference> list = backwardReferencSet as List
        assert list.size() > 0
        String json = list.get(0).toJson()
        Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
                .fileName("test_toJson.json").build()
        sh.write(JsonOutput.prettyPrint(json))
        assertNotNull(json)
        assertTrue(json.contains("a_Make Appointment"))
    }
}
