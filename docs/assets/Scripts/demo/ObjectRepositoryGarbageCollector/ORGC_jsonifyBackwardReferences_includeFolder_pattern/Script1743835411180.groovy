import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.gc.ObjectRepositoryGarbageCollector

import groovy.json.JsonOutput
import internal.GlobalVariable

/**
 * ObjectRepositoryGarbageCollector#getBackwardReference() demonstration
 */

ObjectRepositoryGarbageCollector gc =
	new ObjectRepositoryGarbageCollector.Builder()
		.includeObjectRepositoryFolder("**/Page_CURA*")
		.build()

String json = gc.jsonifyBackwardReferences()

Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID)
					.fileName('garbage.json').build()
sh.write(JsonOutput.prettyPrint(json))
