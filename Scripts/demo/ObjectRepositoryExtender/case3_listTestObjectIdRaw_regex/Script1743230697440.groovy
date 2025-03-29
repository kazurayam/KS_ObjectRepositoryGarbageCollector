import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.ObjectRepositoryExtender
import com.kms.katalon.core.testobject.ObjectRepository

import internal.GlobalVariable

/*
 * Test Caes/demo/ObjectRepositoryExtender/case3
 */

// modify com.kms.katalon.core.testobject.ObjectRepository class on the fly
new ObjectRepositoryExtender().apply()

// step3: select Test Object with ID that match certain pattern by Regular Expression
List<String> idsSelectedByRegex = ObjectRepository.listTestObjectIdRaw('button_\\w+\$', true)

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName('listTestObjectId.txt').build()
sh.write(idsSelectedByRegex)
