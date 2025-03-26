import com.kazurayam.ks.testobject.ObjectRepositoryExtension
import com.kms.katalon.core.testobject.ObjectRepository

/*
 * Test Caes/sampleUse/step1
 */

// modify com.kms.katalon.core.testobject.ObjectRepository class on the fly
ObjectRepositoryExtension.apply()

// step1: get a list of IDs of all Test Objects in the Object Repository
List<String> allTestObjectIDs = ObjectRepository.list()

println "\n---------------------- allTestObjectIDs ------------------------------"
allTestObjectIDs.forEach { s ->
	println s
}
