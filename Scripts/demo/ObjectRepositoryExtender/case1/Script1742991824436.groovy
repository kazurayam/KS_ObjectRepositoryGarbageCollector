import com.kazurayam.ks.testobject.ObjectRepositoryExtender
import com.kms.katalon.core.testobject.ObjectRepository

import demo.Reporter

/*
 * Test Caes/demo/ObjectRepositoryExtender/case1
 */

// modify com.kms.katalon.core.testobject.ObjectRepository class on the fly
new ObjectRepositoryExtender().apply()

// step1: get a list of IDs of all Test Objects in the Object Repository
String listTestObjectId = ObjectRepository.listTestObjectId()

Reporter rp = new Reporter("ObjectRepositoryExtender/case1.md")
rp.report("## ObjectRepository.listTestObjectID() returned", "```", listTestObjectId, "```")
