import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.ObjectRepositoryExtender
import com.kms.katalon.core.testobject.ObjectRepository

import internal.GlobalVariable

// modify com.kms.katalon.core.testobject.ObjectRepository class on the fly
new ObjectRepositoryExtender().apply()

// step1: get a list of IDs of all Test Objects in the Object Repository
String listTestObjectId = ObjectRepository.listTestObjectId()

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName('listTestObjectId.json').build()
sh.write(listTestObjectId)
