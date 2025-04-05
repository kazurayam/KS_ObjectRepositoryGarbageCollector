import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.ExtendedObjectRepository
import com.kms.katalon.core.testobject.ObjectRepository

import internal.GlobalVariable
import groovy.json.JsonOutput

ExtendedObjectRepository xor = new ExtendedObjectRepository()

String json = xor.jsonifyLocatorIndex("btn-", false)

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName('out.json').build()
sh.write(JsonOutput.prettyPrint(json))
