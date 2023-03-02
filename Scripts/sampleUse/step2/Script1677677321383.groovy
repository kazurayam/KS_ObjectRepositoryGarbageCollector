import com.kazurayam.ks.testobject.ObjectRepositoryExtension
import com.kms.katalon.core.testobject.ObjectRepository

/*
 * Test Caes/sampleUse/step2
 */

// modify com.kms.katalon.core.testobject.ObjectRepository class on the fly
ObjectRepositoryExtension.apply()

// step2: select Test Object with ID that match certain pattern by String.contains()
List<String> idsSelectedByStringContains = ObjectRepository.list("button_")

println "\n---------------- idsSelectedByStringContains -------------------------"
idsSelectedByStringContains.forEach { s ->
	println s
}
