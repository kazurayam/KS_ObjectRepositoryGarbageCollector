import com.kazurayam.ks.testobject.gc.ObjectRepositoryGarbageCollector

import groovy.json.JsonOutput

/**
 * A demonstration of ObjectRepositoryGarbageCollector.
 *
 * This TestCase outputs a JSON file which contains a list of garbage Test Objects
 * in the "Object Repository" folder.
 *
 * A "garbage" means a Test Object which is not used by any scripts
 * in the "Test Cases" folder.
 */

// the Garbage Collector instance will scan 2 folders: "Object Repository" and "Test Cases"
ObjectRepositoryGarbageCollector gc = new ObjectRepositoryGarbageCollector.Builder().build()

// gc.jsonifyGarbages() triggers scanning through the 2 folders and analyze the files.
// All forward references from TestCase scripts to TestObject entities are identified.
// Consequently, it can result a list of unused TestObjects.
// Will output the result in a JSON string
String json = gc.jsonifyGarbages()

println JsonOutput.prettyPrint(json)
