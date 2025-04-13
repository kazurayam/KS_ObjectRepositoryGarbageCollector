import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.ObjectRepositoryDecorator
import com.kazurayam.ks.testobject.TestObjectId
import com.kms.katalon.core.testobject.ObjectRepository

import internal.GlobalVariable

// modify com.kms.katalon.core.testobject.ObjectRepository object on the fly
ObjectRepositoryDecorator ord = new ObjectRepositoryDecorator.Builder()
								.includeFolder("**/Page_CURA*")
								.build()

// select TestObjects with id that match certain pattern.
// the pattern is written in Regular Expression
List<TestObjectId> list = ord.getTestObjectIdList('button_\\w+\$', true)

StringBuilder sb = new StringBuilder()
list.each { s ->
	sb.append(s)
	sb.append("\n")
}

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName('out.txt').build()
sh.write(sb.toString())
