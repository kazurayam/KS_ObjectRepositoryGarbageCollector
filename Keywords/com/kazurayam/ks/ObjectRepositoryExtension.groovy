package com.kazurayam.ks

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.SelectorMethod
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files
import java.util.regex.Matcher
import java.util.regex.Pattern
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
	List<String> list(String pattern = ".*", Boolean isRegex = true) throws IOException {
		Path dir = Paths.get("./Object Repository")
		ObjectRepositoryVisitor visitor = new ObjectRepositoryVisitor(dir)
		Files.walkFileTree(dir, visitor)
		List<String> ids = visitor.getIDs()
		//
		List<String> result = new ArrayList<>()
		BiMatcher bim = new BiMatcher(pattern, isRegex)
		ids.forEach { id ->
			if (bim.matches(id)) {
				result.add(id)
			}
		}
		return result;
	}


	Map<String, Set<String>> xref(String pattern = ".*", Boolean isRegex = true) throws IOException {
		Map<String, Set<String>> xref = new TreeMap<>()
		BiMatcher bim = new BiMatcher(pattern, isRegex)
		List<String> idList = this.list()  // list of IDs of Test Object
		idList.forEach { id ->
			String locator = findSelector(id)
			Set<String> idSet
			if (xref.containsKey(locator)) {
				idSet = xref.get(locator)
			} else {
				idSet = new TreeSet<>()
			}
			if (bim.matches(locator)) {
				idSet.add(id)
			}
			xref.put(locator, idSet)
		}
		return xref
	}

	private String findSelector(String testObjectId) {
		TestObject tObj = ObjectRepository.findTestObject(testObjectId)
		SelectorMethod selectorMethod = tObj.getSelectorMethod()
		return tObj.getSelectorCollection().getAt(selectorMethod)
	}

	String xrefAsJson(String pattern = ".*", Boolean isRegex = true) throws IOException {
		Map<String, Set<String>> xref = this.xref(pattern, isRegex)
		String json = JsonOutput.toJson(xref)
		String pp = JsonOutput.prettyPrint(json)
		return pp
	}
}


