import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.combine.ObjectRepositoryGarbageCollector

import groovy.json.JsonOutput
import internal.GlobalVariable

/**
 * ObjectRepositoryGarbageCollector#getBackwardReference() demonstration
 */

ObjectRepositoryGarbageCollector gc =
	new ObjectRepositoryGarbageCollector.Builder()
		.includeObjectRepositoryFolder("**/Page_CURA*")
		.build()

String json = gc.jsonifyBackwardReferencesDatabase()

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
					.fileName('backwardReferences.json').build()
sh.write(JsonOutput.prettyPrint(json))
