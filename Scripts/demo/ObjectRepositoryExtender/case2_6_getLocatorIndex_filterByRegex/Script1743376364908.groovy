import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.LocatorIndex
import com.kazurayam.ks.testobject.ObjectRepositoryExtender
import com.kazurayam.ks.testobject.TestObjectEssence
import com.kms.katalon.core.testobject.ObjectRepository

import internal.GlobalVariable

/*
 * Test Cases/demo/ObjectRepositoryExtender/case2_6
 */

// modify com.kms.katalon.core.testobject.ObjectRepository object on the fly
new ObjectRepositoryExtender().apply()

// step6: 
// get the LocatorIndex selected by Regular Expression
LocatorIndex locatorIndex = ObjectRepository.getLocatorIndex("'btn-\\w+'", true)

StringBuilder sb = new StringBuilder()
locatorIndex.iterator().each { entry ->
	sb.append(entry.key.toString() + " : \n")
	Set<TestObjectEssence> essences = entry.value
	essences.each { essence -> sb.append("\t${essence}") }
	sb.append("\n\n")
}

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName('out.txt').build()
sh.write(sb.toString())
