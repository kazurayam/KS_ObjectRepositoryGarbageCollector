import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.LocatorDeclarations
import com.kazurayam.ks.testobject.LocatorIndex
import com.kazurayam.ks.testobject.ObjectRepositoryDecorator

import internal.GlobalVariable

ObjectRepositoryDecorator ord = new ObjectRepositoryDecorator.Builder()
								.includeFolder("**/Page_CURA*")
								.build()

LocatorIndex locatorIndex = ord.getLocatorIndex()

StringBuilder sb = new StringBuilder()
locatorIndex.iterator().each { entry ->
	sb.append(entry.key.toString() + " : \n")
	Set<LocatorDeclarations> ldSet = entry.value
	ldSet.each { locatorDeclarations -> sb.append("\t${locatorDeclarations}") }
	sb.append("\n\n")
}

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName('out.txt').build()
sh.write(sb.toString())
