import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.ObjectRepositoryExtender
import com.kazurayam.ks.testobject.TestObjectId
import com.kms.katalon.core.testobject.ObjectRepository

import internal.GlobalVariable

/*
 * Test Caes/demo/ObjectRepositoryExtender/case3
 */

// modify com.kms.katalon.core.testobject.ObjectRepository class on the fly
new ObjectRepositoryExtender().apply()

// step3: select Test Object with ID that match certain pattern by Regular Expression
List<TestObjectId> list = ObjectRepository.getTestObjectIdList('button_\\w+\$', true)

StringBuilder sb = new StringBuilder()
list.each { s ->
	sb.append(s)
	sb.append("\n")
}

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName('out.txt').build()
sh.write(sb.toString())
