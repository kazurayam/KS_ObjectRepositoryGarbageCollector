import com.kazurayam.ks.testobject.ObjectRepositoryExtender
import com.kms.katalon.core.testobject.ObjectRepository
import groovy.json.JsonOutput
import com.kazurayam.ks.reporting.Shorthand

import internal.GlobalVariable

/*
 * Test Caes/demo/ObjectRepositoryExtender/case4
 */

// modify com.kms.katalon.core.testobject.ObjectRepository class on the fly
new ObjectRepositoryExtender().apply()

// step4: reverse Test Object Lookup by locator, full result
Map<String, Set<String>> result = ObjectRepository.getBackwardReferences()
String json = JsonOutput.prettyPrint(JsonOutput.toJson(result))

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName('out.json').build()
sh.write(json)
