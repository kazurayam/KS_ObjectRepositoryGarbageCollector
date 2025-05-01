package com.kazurayam.ks.testobject.combine

import com.kazurayam.ks.reporting.Shorthand
import groovy.json.JsonOutput
import org.junit.Test
import org.junit.Before
import static org.junit.Assert.*

class RunDescriptionTest {

    private RunDescription runDescription

    /**
     * {
     *     "Project name": "katalon",
     *     "includeScriptsFolder": [
     *         "main",
     *         "misc"
     *     ],
     *     "includeObjectRepositoryFolder": [
     *         "main",
     *         "misc"
     *     ],
     *     "Number of TestCases": 5,
     *     "Number of TestObjects": 16,
     *     "Number of unused TestObjects": 5
     * }
     */
    @Before
    void setup() {
        runDescription = new RunDescription.Builder("katalon")
                .includeScriptsFolder(["main", "misc"])
                .includeObjectRepositoryFolder(["main", "misc"])
                .numberOfTestCases(5)
                .numberOfTestObjects(16)
                .numberOfUnusedTestObjects(5)
                .build()
    }

    @Test
    void test_toJson() {
        String json = JsonOutput.prettyPrint(runDescription.toJson())
        Shorthand sh = new Shorthand .Builder().subDir(this.getClass().getName())
                .fileName("test_toJson.json").build()
        sh.write(json)
        assertTrue(json.contains("katalon"))
        assertTrue(json.contains("main"))
        assertTrue(json.contains("misc"))
        assertTrue(json.contains("5"))
        assertTrue(json.contains("16"))
    }
}
