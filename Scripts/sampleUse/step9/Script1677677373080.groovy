import com.kazurayam.ks.testobject.ObjectRepositoryExtension
import com.kms.katalon.core.testobject.ObjectRepository

/*
 * Test Caes/sampleUse/step9
 */

// modify com.kms.katalon.core.testobject.ObjectRepository class on the fly
ObjectRepositoryExtension.apply()

// step9: reverse Test Object Lookup by locator in JSON format selected by RegularExpression
String json9  = ObjectRepository.reverseLookup("'btn-\\w+'", true)

println "\n------ reverseLookup, selected by Regular Expression -----"
println json9
