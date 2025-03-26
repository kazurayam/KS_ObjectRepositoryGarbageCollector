import com.kazurayam.ks.testobject.ObjectRepositoryExtender
import com.kms.katalon.core.testobject.ObjectRepository
import demo.Reporter

/*
 * Test Caes/demo/ObjectRepositoryExtender/case2
 */

// modify com.kms.katalon.core.testobject.ObjectRepository class on the fly
new ObjectRepositoryExtender().apply()

// step2: select Test Object with ID that match certain pattern by String.contains()
List<String> list = ObjectRepository.listTestObjectIdRaw("button_")

StringBuilder sb = new StringBuilder()
list.each { s ->
	sb.append(s)
	sb.append("\n")
}

Reporter rp = new Reporter("ObjectRepositoryExtender/case2.md")
rp.report('## ObjectRepository.listTestObjectIdRaw("button_") returned',
	'```', sb.toString(), '```')
