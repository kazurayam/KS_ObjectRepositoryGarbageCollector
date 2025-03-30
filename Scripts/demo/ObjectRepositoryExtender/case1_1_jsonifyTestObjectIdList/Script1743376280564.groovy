import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.ObjectRepositoryExtender
import com.kms.katalon.core.testobject.ObjectRepository

import internal.GlobalVariable
import groovy.json.JsonOutput

// Test Cases/demo/ObjectRepositoryExternder/case1_1

// modify com.kms.katalon.core.testobject.ObjectRepository object on the fly
new ObjectRepositoryExtender().apply()

// step1: 
// get a list of all Test Object Ids found in the Object Repository,
// output the result in JSON format
String out = ObjectRepository.jsonifyTestObjectIdList()

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName('out.json').build()
sh.write(JsonOutput.prettyPrint(out))
