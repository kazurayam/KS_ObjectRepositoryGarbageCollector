import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.ObjectRepositoryExtender
import com.kazurayam.ks.testobject.TestObjectId
import com.kms.katalon.core.testobject.ObjectRepository

import internal.GlobalVariable

// modify com.kms.katalon.core.testobject.ObjectRepository object on the fly
new ObjectRepositoryExtender().apply()

// step2
// select TestObjects with id that match certain pattern by String.contains()
// the pattern is interpreted as a plain string
List<TestObjectId> list = ObjectRepository.getTestObjectIdList("button_")

StringBuilder sb = new StringBuilder()
list.each { s ->
	sb.append(s)
	sb.append("\n")
}

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName('out.txt').build()
sh.write(sb.toString())
