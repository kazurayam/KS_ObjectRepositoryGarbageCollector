import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.LocatorIndex
import com.kazurayam.ks.testobject.ExtendedObjectRepository
import com.kazurayam.ks.testobject.TestObjectEssence
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.testobject.ObjectRepository

import groovy.json.JsonOutput
import internal.GlobalVariable

ExtendedObjectRepository xor = new ExtendedObjectRepository.Builder()
								.includeFolder("**/Page_CURA*")
								.build()

LocatorIndex locatorIndex = xor.getLocatorIndex()

StringBuilder sb = new StringBuilder()
locatorIndex.iterator().each { entry ->
	sb.append(entry.key.toString() + " : \n")
	Set<TestObjectEssence> essences = entry.value
	essences.each { essence -> sb.append("\t${essence}") }
	sb.append("\n\n")
}

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName('out.txt').build()
sh.write(sb.toString())
