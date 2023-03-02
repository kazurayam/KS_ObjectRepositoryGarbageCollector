import com.kazurayam.ks.testobject.ObjectRepositoryExtension
import com.kms.katalon.core.testobject.ObjectRepository

/*
 * Test Caes/sampleUse/step7
 */

// modify com.kms.katalon.core.testobject.ObjectRepository class on the fly
ObjectRepositoryExtension.apply()

// step7: reverse Test Object Lookup by locator in JSON format
String json7  = ObjectRepository.reverseLookupAsJson()

println "\n----------------- reverseLookupAsJson, full -------------------"
println json7
