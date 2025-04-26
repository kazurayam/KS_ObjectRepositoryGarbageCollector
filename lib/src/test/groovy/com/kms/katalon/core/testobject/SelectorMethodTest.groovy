package com.kms.katalon.core.testobject

import static org.junit.Assert.*

import org.junit.Test
import com.kms.katalon.core.testobject.SelectorMethod as KsSelectorMethod

class SelectorMethodTest {

    @Test
    void test_values() {
        for (KsSelectorMethod method : KsSelectorMethod.values()) {
            println method
        }
    }

    @Test
    void test_name() {
        assertEquals("BASIC", KsSelectorMethod.BASIC.name())
        assertEquals("XPATH", KsSelectorMethod.XPATH.name())
        assertEquals("CSS", KsSelectorMethod.CSS.name())
        assertEquals("IMAGE", KsSelectorMethod.IMAGE.name())
        assertEquals("SMART_LOCATOR", KsSelectorMethod.SMART_LOCATOR.name())
    }

}
