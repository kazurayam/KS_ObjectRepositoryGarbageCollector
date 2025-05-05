package com.kazurayam.ks.testobject.combine

import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.TestObjectId
import groovy.json.JsonOutput
import org.junit.Test

import java.nio.file.Paths

import static org.junit.Assert.assertEquals

class CombinedLocatorDeclarationsTest {

    private CombinedLocatorDeclarations cld

    @Test
    void test_caseOfUnusedTestObject() {
        //Locator locator = new Locator("//body", SelectorMethod.XPATH)
        TestObjectId testObjectId = new TestObjectId(Paths.get("misc/dummy1.rs"))
        cld = new CombinedLocatorDeclarations(testObjectId)
        BackwardReference br = new BackwardReference(testObjectId)
        cld.add(br)
        //
        assertEquals(1, cld.getDeclarations().size())
        Set<BackwardReference> brSet = cld.getDeclarations()
        brSet.each { brEntry ->
            assertEquals(0, brEntry.getNumberOfReferences())
        }
        //
        String json = cld.toJson()
        Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
                .fileName("caseOfUnusedTestObject.json").build()
        sh.write(JsonOutput.prettyPrint(json))
    }
}
