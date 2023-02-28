package com.kazurayam.ks

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.SelectorMethod
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files
import groovy.json.JsonOutput

/**
 * This class extends the `com.kms.katalon.core.testobject.ObjectRepository` class and
 * add some useful methods on the fly by Groovy's Meta-programming technique.
 * 
 * @author kazurayam
 */
public class ObjectRepositoryExtension {

	ObjectRepositoryExtension() {}

	@Keyword
	void apply() {
		ObjectRepository.metaClass.static.invokeMethod = { String name, args ->
			switch (name) {
				case "list" :
					return this.list()
					break
				case "xref" :
					return this.xref()
					break
				case "xrefAsJson" :
					return this.xrefAsJson()
					break
				default :
				// just do what ObejctRepository is designed to do
					def result
					try {
						result = delegate.metaClass.getMetaMethod(name, args).invoke(delegate, args)
					} catch (Exception e) {
						System.err.println("call to method $name raised an Exception")
						e.printStackTrace()
					}
					return result
			}
		}
	}

	/*
	 * 
	 */
	List<String> list() throws IOException {
		Path dir = Paths.get("./Object Repository")
		ObjectRepositoryVisitor visitor = new ObjectRepositoryVisitor(dir)
		Files.walkFileTree(dir, visitor)
		return visitor.getIDs()
	}

	Map<String, Set<String>> xref() throws IOException {
		Map<String, Set<String>> xref = new TreeMap<>()
		List<String> idList = this.list()
		idList.forEach { testObjectId ->
			TestObject tObj = ObjectRepository.findTestObject(testObjectId)
			SelectorMethod selectorMethod = tObj.getSelectorMethod()
			String locator = tObj.getSelectorCollection().getAt(selectorMethod)
			Set<String> idSet
			if (xref.containsKey(locator)) {
				idSet = xref.get(locator)
			} else {
				idSet = new TreeSet<>()
			}
			idSet.add(testObjectId)
			xref.put(locator, idSet)
		}
		return xref
	}

	String xrefAsJson() throws IOException {
		Map<String, Set<String>> xref = this.xref()
		String json = JsonOutput.toJson(xref)
		String pp = JsonOutput.prettyPrint(json)
		return pp
	}
}


