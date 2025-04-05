import com.kazurayam.ks.testobject.TestObjectId
import com.kazurayam.ks.testobject.gc.Garbages
import com.kazurayam.ks.testobject.gc.ObjectRepositoryGarbageCollector

import groovy.json.JsonOutput

/**
 * ObjectRepositoryGarbageCollector#getGarbage() demonstration
 */

ObjectRepositoryGarbageCollector gc =
	new ObjectRepositoryGarbageCollector.Builder()
		.includeFolder("**/Page_CURA*")
		.build()

Garbages garbages = gc.getGarbages()

Set<TestObjectId> testObjectIds = garbages.getAllTestObjectIds()

for (TestObjectId toi : testObjectIds) {
	println toi
}

assert testObjectIds.size() == 4