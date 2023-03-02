import com.kazurayam.ks.testobject.ObjectRepositoryExtension
import com.kms.katalon.core.testobject.ObjectRepository

/*
 * Test Caes/sampleUse/step8
 */

// modify com.kms.katalon.core.testobject.ObjectRepository class on the fly
ObjectRepositoryExtension.apply()

// step8: reverse Test Object Lookup by locator in JSON format selected by String.contains()
String json8  = ObjectRepository.reverseLookupAsJson("btn-")

println "\n------ reverseLookupAsJson, selected by String.contains() -----"
println json8
