import com.kazurayam.ks.testobject.TestObjectId
import com.kazurayam.ks.testobject.combine.Garbage
import com.kazurayam.ks.testobject.combine.ObjectRepositoryGarbageCollector

import groovy.json.JsonOutput

/**
 * ObjectRepositoryGarbageCollector#getGarbage() demonstration
 */

ObjectRepositoryGarbageCollector gc =
	new ObjectRepositoryGarbageCollector.Builder()
		.includeObjectRepositoryFolder("**/Page_CURA*")
		.build()

Garbage garbage = gc.getGarbage()

Set<TestObjectId> testObjectIds = garbage.getAllTestObjectIds()

for (TestObjectId toi : testObjectIds) {
	println toi
}

assert testObjectIds.size() == 4