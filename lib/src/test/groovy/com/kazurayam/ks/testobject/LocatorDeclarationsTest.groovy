package com.kazurayam.ks.testobject

import com.kazurayam.ks.reporting.Shorthand
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.nio.file.Paths

import static org.junit.Assert.assertTrue

@RunWith(JUnit4.class)
class LocatorDeclarationsTest {

    private LocatorDeclarations locatorDeclarations

    @Before
    void setup() {
        Locator locator = new Locator("//body", SelectorMethod.XPATH)
        locatorDeclarations = new LocatorDeclarations(locator)
        TestObjectId testObjectId = new TestObjectId(Paths.get("misc/dummy1.rs"))
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
