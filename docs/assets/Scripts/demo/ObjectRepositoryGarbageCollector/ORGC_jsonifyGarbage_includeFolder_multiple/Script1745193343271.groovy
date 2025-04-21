import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.gc.Garbage
import com.kazurayam.ks.testobject.gc.ObjectRepositoryGarbageCollector
import com.kms.katalon.core.configuration.RunConfiguration

import groovy.json.JsonOutput
import internal.GlobalVariable


/**
 * Similar to the GC script but
 * the TestObjects under the specified sub-folder in the "Object Repository" are selected.
 */
ObjectRepositoryGarbageCollector gc =
		new ObjectRepositoryGarbageCollector.Builder()
			.includeObjectRepositoryFolder("main/Page_CURA Healthcare Service")
			.includeObjectRepositoryFolder("main/Page_CURA Healthcare Service/xtra")
			.build()

String json = gc.jsonifyGarbage()

Shorthand sh = new Shorthand.Builder()
					.subDir(GlobalVariable.TESTCASE_ID)
					.fileName('garbage.json').build()
sh.write(JsonOutput.prettyPrint(json))

Garbage garbage = gc.getGarbage()
assert 4 == garbage.size() : "expected garbage.size()==4 but was ${garbage.size()}"

