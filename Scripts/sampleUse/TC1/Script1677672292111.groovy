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




// step4: reverse Test Object Lookup by locator, full result
Map<String, Set<String>> result4 = ObjectRepository.reverseLookup()
println "\n---------------- reverseLookup, full ------------------------"
result4.keySet().forEach { locator ->
	println "${locator}"
	Set<String> ids = result4.get(locator)
	ids.forEach { id -> println "\t${id}" }
	println ""
}

// step5: reverse Test Object Lookup by locator, selected by String.contains()
Map<String, Set<String>> result5 = ObjectRepository.reverseLookup("[@id='btn-", false)
println "\n-------- reverseLookup, selected by String.contains ----------"
result5.keySet().forEach { locator ->
	println "${locator}"
	Set<String> ids = result5.get(locator)
	ids.forEach { id -> println "\t${id}" }
	println ""
}

// step6: reverse Test Object Lookup by locator, selected by Regular Expression
Map<String, Set<String>> result6 = ObjectRepository.reverseLookup("'btn-\\w+'", true)
println "\n-------- reverseLookup, selected by Regular Expression ----------"
result6.keySet().forEach { locator ->
	println "${locator}"
	Set<String> ids = result6.get(locator)
	ids.forEach { id -> println "\t${id}" }
	println ""
}

// step7: reverse Test Object Lookup by locator in JSON format
String json7  = ObjectRepository.reverseLookupAsJson()
println "\n----------------- reverseLookupAsJson, full -------------------"
println json7






// step8: reverse Test Object Lookup by locator in JSON format selected by String.contains()
String json8  = ObjectRepository.reverseLookupAsJson("btn-")
println "\n------ reverseLookupAsJson, selected by String.contains() -----"
println json8






// step9: reverse Test Object Lookup by locator in JSON format selected by RegularExpression
String json9  = ObjectRepository.reverseLookupAsJson("'btn-\\w+'", true)
println "\n------ reverseLookupAsJson, selected by Regular Expression -----"
println json9
