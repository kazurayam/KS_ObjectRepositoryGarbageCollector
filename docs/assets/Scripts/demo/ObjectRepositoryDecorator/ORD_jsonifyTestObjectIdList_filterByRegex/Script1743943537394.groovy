import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.ObjectRepositoryDecorator

import groovy.json.JsonOutput
import internal.GlobalVariable

ObjectRepositoryDecorator ord = new ObjectRepositoryDecorator.Builder()
								.includeFolder("**/Page_CURA*")
								.build()

// get a list of all Test Object Ids found in the Object Repository,
// output the result in JSON format
String json = ord.jsonifyTestObjectIdList("button_\\w+\$", true)

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName('out.json').build()
sh.write(JsonOutput.prettyPrint(json))
