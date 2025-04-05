import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.ExtendedObjectRepository
import com.kms.katalon.core.testobject.ObjectRepository

import internal.GlobalVariable
import groovy.json.JsonOutput

ExtendedObjectRepository xor = new ExtendedObjectRepository()

// get a list of all Test Object Ids found in the Object Repository,
// output the result in JSON format
String json = xor.jsonifyTestObjectIdList("button_\\w+\$", true)

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName('out.json').build()
sh.write(JsonOutput.prettyPrint(json))
