// demo/ObjectRepositoryGarbageCollector/GC_selective
import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.gc.ObjectRepositoryGarbageCollector

import internal.GlobalVariable

/**
 * Similar to the GC script but this scans specific sub-directory of "Object Repository", 
 * specificy sub-directory of "Test Cases".
 */
ObjectRepositoryGarbageCollector gc = new ObjectRepositoryGarbageCollector.Builder()
								.objectRepositorySubpath("Page_CURA Healthcare Service")
								.testCasesSubpath("main")
								.build()

String json = gc.garbages()
								
Shorthand sh = new Shorthand.Builder().subDir(GlobalVariable.TESTCASE_ID).fileName("garbages.json").build()
sh.write(json)
								