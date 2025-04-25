package com.kazurayam.ks.testobject

import com.kazurayam.ks.reporting.Shorthand
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

@RunWith(JUnit4.class)
class LocatorDeclarationsTest {

    private LocatorDeclarations locatorDeclarations

    @Before
    void setup() {
        Locator locator = new Locator("//body")
        locatorDeclarations = new LocatorDeclarations(locator)
        TestObjectId testObjectId = new TestObjectId("misc/dummy1")
        locatorDeclarations.add(testObjectId)
    }

    @Test
    void test_toJson() {
        String json = locatorDeclarations.toJson()
        Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
                .fileName("test_toJson.json").build()
        sh.write(JsonOutput.prettyPrint(json))
        assertTrue(json.contains("//body"))
    }
}
