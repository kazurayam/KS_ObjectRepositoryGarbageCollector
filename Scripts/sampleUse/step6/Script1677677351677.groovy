import com.kazurayam.ks.testobject.ObjectRepositoryExtension
import com.kms.katalon.core.testobject.ObjectRepository

/*
 * Test Caes/sampleUse/step6
 */

// modify com.kms.katalon.core.testobject.ObjectRepository class on the fly
ObjectRepositoryExtension.apply()

// step6: reverse Test Object Lookup by locator, selected by Regular Expression
Map<String, Set<String>> result6 = ObjectRepository.reverseLookup("'btn-\\w+'", true)
println "\n-------- reverseLookup, selected by Regular Expression ----------"
result6.keySet().forEach { locator ->
	println "${locator}"
	Set<String> ids = result6.get(locator)
	ids.forEach { id -> println "\t${id}" }
	println ""
}
