import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.ExtendedObjectRepository
import com.kms.katalon.core.testobject.ObjectRepository

import groovy.json.JsonOutput
import internal.GlobalVariable

ExtendedObjectRepository xor = new ExtendedObjectRepository.Builder()
								.includeFolder("**/Page_CURA*")
								.build()

String json = xor.jsonifyLocatorIndex()

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName('out.json').build()
sh.write(JsonOutput.prettyPrint(json))
