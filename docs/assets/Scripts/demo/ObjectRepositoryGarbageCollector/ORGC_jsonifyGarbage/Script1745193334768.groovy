import com.kazurayam.ks.logging.SimplifiedStopWatch
import com.kazurayam.ks.testobject.combine.ObjectRepositoryGarbageCollector
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import groovy.json.JsonOutput

/**
 * A demonstration of ObjectRepositoryGarbageCollector, the simplest case
 *
 * This TestCase outputs a JSON file which contains a list of garbage Test Objects
 * in the "Object Repository" folder.
 *
 * A "garbage" means a Test Object which is not used by any scripts
 * in the "Test Cases" folder.
 */

// the Garbage Collector instance will scan 2 folders: "Object Repository" and "Test Cases".

// Amongst the folders in the "Object Repository" folder, the TestObjectsCase scripts contained 
// in the subfolder that match "**/Page_CURA*" will be selected, and others are ignored
ObjectRepositoryGarbageCollector gc = 
		new ObjectRepositoryGarbageCollector.Builder()
			.includeObjectRepositoryFolder("**/*")
			.build()

SimplifiedStopWatch ssw = new SimplifiedStopWatch()
			
// gc.jsonifyGarbage() triggers scanning through the entire "Object Repository".
// All references from TestCase scripts to TestObjects will be identified.
// Consequently, it can result a list of unused TestObjects.
// Will output the result into a JSON string
String json = gc.jsonifyGarbage()

// just print the JSON into the console
println JsonOutput.prettyPrint(json)

ssw.stop()
WebUI.comment(ssw.toString())


