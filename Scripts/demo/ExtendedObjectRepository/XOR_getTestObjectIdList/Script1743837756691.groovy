import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.ExtendedObjectRepository
import com.kazurayam.ks.testobject.TestObjectId
import com.kms.katalon.core.testobject.ObjectRepository

import internal.GlobalVariable

ExtendedObjectRepository xor = new ExtendedObjectRepository.Builder()
								.includeFolder("**/Page_CURA*")
								.build()

List<TestObjectId> list = xor.getTestObjectIdList("button_")

StringBuilder sb = new StringBuilder()
list.each { s ->
	sb.append(s)
	sb.append("\n")
}

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName('out.txt').build()
sh.write(sb.toString())
