
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.testobject.gc.ObjectRepositoryGarbageCollector
import com.kms.katalon.core.configuration.RunConfiguration

import groovy.json.JsonOutput
import internal.GlobalVariable
import com.kazurayam.ks.reporting.Shorthand

/**
 * ObjectRepositoryGarbageCollector#getBackwardReference() demonstration
 */

ObjectRepositoryGarbageCollector gc =
	new ObjectRepositoryGarbageCollector.Builder()
		.includeObjectRepositoryFolder("**/Page_CURA*")
		.build()

String json = gc.jsonifyLocatorIndex("td[31]", false)

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
					.fileName('garbage.json').build()
sh.write(JsonOutput.prettyPrint(json))
