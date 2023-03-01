import com.kazurayam.ks.testobject.ObjectRepositoryExtension
import com.kms.katalon.core.testobject.ObjectRepository

/*
 * Test Caes/sampleUse/TC1
 */



// modify com.kms.katalon.core.testobject.ObjectRepository class on the fly
ObjectRepositoryExtension.apply()

// step1: get a list of IDs of all Test Objects in the Object Repository
List<String> allTestObjectIDs = ObjectRepository.list()
println "\n---------------------- allTestObjectIDs ------------------------------"
allTestObjectIDs.forEach { s ->
	println s
}

// step2: select Test Object with ID that match certain pattern by String.contains()
List<String> idsSelectedByStringContains = ObjectRepository.list("button_")
println "\n---------------- idsSelectedByStringContains -------------------------"
idsSelectedByStringContains.forEach { s ->
	println s
}




// step3: select Test Object with ID that match certain pattern by Regular Expression
List<String> idsSelectedByRegex = ObjectRepository.list('button_\\w+\$', true)
println "\n---------------- idsSelectedByRegex -------------------------"
idsSelectedByRegex.forEach { s ->
	println s
}




// step4: reverse Test Object lookup by locator
