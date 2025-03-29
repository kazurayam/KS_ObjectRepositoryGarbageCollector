// demo/ObjectRepositoryGarbageCollector/GC

import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.gc.ObjectRepositoryGarbageCollector

import internal.GlobalVariable

/**
 * outputs a JSON file which contains a list of garbage Test Objects
 * in the Object Repository directory.
 * A garbage Test Object is a Test Object which is unused by any of Test Cases.
 */
// the Garbage Collector instance will scan the Object Repository directory
// and the Scripts directory
ObjectRepositoryGarbageCollector gc = new ObjectRepositoryGarbageCollector.Builder().build()

// the gc.garbages() method call can compile a list of garbate Test Objects,
// output the information in a JSON string
String json = gc.garbages()

// write it into a file
Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName('garbage.json').build()

sh.write(json)