import com.kazurayam.ks.testobject.ObjectRepositoryExtender
import com.kms.katalon.core.testobject.ObjectRepository

/*
 * Test Caes/sampleUse/ObjectRepositoryExtender/case8
 */

// modify com.kms.katalon.core.testobject.ObjectRepository class on the fly
new ObjectRepositoryExtender().apply()

// step8: reverse Test Object Lookup by locator in JSON format selected by String.contains()
String json8  = ObjectRepository.reverseLookup("btn-")

println "\n------ reverseLookup, selected by String.contains() -----"
println json8
