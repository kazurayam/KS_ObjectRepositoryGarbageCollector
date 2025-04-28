import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.testobject.combine.ObjectRepositoryGarbageCollector
import com.kms.katalon.core.configuration.RunConfiguration

import groovy.json.JsonOutput
import internal.GlobalVariable
import com.kazurayam.ks.reporting.Shorthand

/**
 * ObjectRepositoryGarbageCollector#jsonifyCombinedLocatorIndex() demonstration
 */

ObjectRepositoryGarbageCollector gc =
	new ObjectRepositoryGarbageCollector.Builder()
		.includeObjectRepositoryFolder("**/Page_CURA*")
		.build()

String json = gc.jsonifyCombinedLocatorIndex()

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
					.fileName('CombinedLocatorIndex.json').build()
sh.write(JsonOutput.prettyPrint(json))