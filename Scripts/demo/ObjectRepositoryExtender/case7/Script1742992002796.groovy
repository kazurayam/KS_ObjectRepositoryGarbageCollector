import com.kazurayam.ks.testobject.ObjectRepositoryExtender
import com.kms.katalon.core.testobject.ObjectRepository

/*
 * Test Caes/demo/ObjectRepositoryExtender/case7
 */

// modify com.kms.katalon.core.testobject.ObjectRepository class on the fly
new ObjectRepositoryExtender().apply()

// step7: reverse Test Object Lookup by locator in JSON format
String json7  = ObjectRepository.reverseLookup()

println "\n----------------- reverseLookup, full -------------------"
println json7
