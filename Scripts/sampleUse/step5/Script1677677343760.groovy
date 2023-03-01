import com.kazurayam.ks.testobject.ObjectRepositoryExtension
import com.kms.katalon.core.testobject.ObjectRepository

/*
 * Test Caes/sampleUse/step5
 */
	
// modify com.kms.katalon.core.testobject.ObjectRepository class on the fly
ObjectRepositoryExtension.apply()

// step5: reverse Test Object Lookup by locator, selected by String.contains()
Map<String, Set<String>> result5 = ObjectRepository.reverseLookup("[@id='btn-", false)
println "\n-------- reverseLookup, selected by String.contains ----------"
result5.keySet().forEach { locator ->
	println "${locator}"
	Set<String> ids = result5.get(locator)
	ids.forEach { id -> println "\t${id}" }
	println ""
}
