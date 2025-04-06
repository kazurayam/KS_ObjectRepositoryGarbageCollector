import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.ObjectRepositoryAdapter
import com.kms.katalon.core.testobject.ObjectRepository

import internal.GlobalVariable
import groovy.json.JsonOutput

ObjectRepositoryAdapter ora = new ObjectRepositoryAdapter.Builder()
								.includeFolder("**/Page_CURA*")
								.build()

// get a list of all Test Object Ids found in the Object Repository,
// output the result in JSON format
String json = ora.jsonifyTestObjectIdList("button_\\w+\$", true)

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName('out.json').build()
sh.write(JsonOutput.prettyPrint(json))
