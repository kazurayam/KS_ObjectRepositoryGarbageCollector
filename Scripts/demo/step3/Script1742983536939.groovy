import com.kazurayam.ks.testobject.ObjectRepositoryExtension
import com.kms.katalon.core.testobject.ObjectRepository

/*
 * Test Caes/sampleUse/TC1
 */



// modify com.kms.katalon.core.testobject.ObjectRepository class on the fly
ObjectRepositoryExtension.apply()

// step3: select Test Object with ID that match certain pattern by Regular Expression
List<String> idsSelectedByRegex = ObjectRepository.list('button_\\w+\$', true)

println "\n---------------- idsSelectedByRegex -------------------------"
idsSelectedByRegex.forEach { s ->
	println s
}
