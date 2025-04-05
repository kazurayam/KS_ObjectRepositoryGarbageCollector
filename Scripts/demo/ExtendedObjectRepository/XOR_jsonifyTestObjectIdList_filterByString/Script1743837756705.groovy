import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.ExtendedObjectRepository
import com.kms.katalon.core.testobject.ObjectRepository

import internal.GlobalVariable
import groovy.json.JsonOutput

// modify com.kms.katalon.core.testobject.ObjectRepository object on the fly
ExtendedObjectRepository xor = new ExtendedObjectRepository.Builder()
								.includeFolder("**/Page_CURA*")
								.build()

// get a list of all Test Object Ids found in the Object Repository,
// output the result in JSON format
String out = xor.jsonifyTestObjectIdList('button_', false)

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName('out.json').build()
sh.write(JsonOutput.prettyPrint(out))
