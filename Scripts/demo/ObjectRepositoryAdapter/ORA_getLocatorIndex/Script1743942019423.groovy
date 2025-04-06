import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.LocatorIndex
import com.kazurayam.ks.testobject.ObjectRepositoryAdapter
import com.kazurayam.ks.testobject.TestObjectEssence
import com.kms.katalon.core.testobject.ObjectRepository

import groovy.json.JsonOutput
import internal.GlobalVariable

ObjectRepositoryAdapter ora = new ObjectRepositoryAdapter.Builder()
								.includeFolder("**/Page_CURA*")
								.build()

LocatorIndex locatorIndex = ora.getLocatorIndex()

StringBuilder sb = new StringBuilder()
locatorIndex.iterator().each { entry ->
	sb.append(entry.key.toString() + " : \n")
	Set<TestObjectEssence> essences = entry.value
	essences.each { essence -> sb.append("\t${essence}") }
	sb.append("\n\n")
}

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName('out.txt').build()
sh.write(sb.toString())
