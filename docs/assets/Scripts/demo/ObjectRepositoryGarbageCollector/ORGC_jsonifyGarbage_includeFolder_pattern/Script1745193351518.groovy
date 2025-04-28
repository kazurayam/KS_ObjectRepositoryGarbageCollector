import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.combine.Garbage
import com.kazurayam.ks.testobject.combine.ObjectRepositoryGarbageCollector

import groovy.json.JsonOutput
import internal.GlobalVariable

/**
 * Similar to the GC script but
 * the TestObjects under the specified sub-folders in the "Object Repository" are selected. 
 * The folder names are matched with the pattern like Ant DirectoryScanner.
 */
ObjectRepositoryGarbageCollector gc =
		new ObjectRepositoryGarbageCollector.Builder()
			.includeObjectRepositoryFolder("**/Page_CURA*")
			.build()
String json = gc.jsonifyGarbage()

Shorthand sh = new Shorthand.Builder()
				.subDir(GlobalVariable.TESTCASE_ID)
				.fileName('garbage.json').build()
sh.write(JsonOutput.prettyPrint(json))

Garbage garbage = gc.getGarbage()
assert 4 == garbage.size() : "expected garbage.size()==4 but was ${garbage.size()}"
