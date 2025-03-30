import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.ObjectRepositoryExtender
import com.kms.katalon.core.testobject.ObjectRepository

import internal.GlobalVariable
import groovy.json.JsonOutput

/*
 * Test Cases/demo/ObjectRepositoryExtender/case2_3
 */

// modify com.kms.katalon.core.testobject.ObjectRepository class on the fly
new ObjectRepositoryExtender().apply()

// step9: reverse Test Object Lookup by locator in JSON format selected by RegularExpression
String json  = ObjectRepository.jsonifyLocatorIndex('btn-\\w+', true)

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName('out.json').build()
sh.write(JsonOutput.prettyPrint(json))
