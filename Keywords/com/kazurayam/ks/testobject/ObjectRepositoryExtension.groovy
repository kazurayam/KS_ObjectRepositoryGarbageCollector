package com.kazurayam.ks.testobject

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.testobject.TestObject

import groovy.json.JsonOutput

/**
 * This class extends the `com.kms.katalon.core.testobject.ObjectRepository` class and
 * add some useful methods on the fly by Groovy's Meta-programming technique.
 * 
 * @author kazurayam
 */
public class ObjectRepositoryExtension {

	private ObjectRepositoryExtension() {}

	@Keyword
	static void apply() {
		ObjectRepository.metaClass.static.invokeMethod = { String name, args ->
			switch (name) {
				case "listRaw" :
					return this.listRaw(args)
					break
				case "list" :
					return this.list(args)
					break
				case "listWithLocatorRaw" :
					return this.listWithLocatorRaw(args)
					break
				case "listWithLocator" :
					return this.listWithLocator(args)
					break
				case "reverseLookupRaw" :
					return this.reverseLookupRaw(args)
					break
				case "reverseLookup" :
					return this.reverseLookup(args)
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

	//-------------------------------------------------------------------------
	static List<String> listRaw(Object ... args) throws Exception {
		ExtendedObjectRepository exor = new ExtendedObjectRepository()
		if (args.length == 0) {
			return exor.listRaw("", false)
		} else if (args.length == 1) {
			return exor.listRaw((String)args[0], false)
		} else {
			return exor.listRaw((String)args[0], (Boolean)args[1])
		}
	}

	//-------------------------------------------------------------------------
	static String list(Object ... args) throws Exception {
		ExtendedObjectRepository exor = new ExtendedObjectRepository()
		if (args.length == 0) {
			return exor.list("", false)
		} else if (args.length == 1) {
			return exor.list((String)args[0], false)
		} else {
			return exor.list((String)args[0], (Boolean)args[1])
		}
	}

	//-------------------------------------------------------------------------
	static List<Map<String, String>> listWithLocatorRaw(Object ... args) throws Exception {
		ExtendedObjectRepository exor = new ExtendedObjectRepository()
		if (args.length == 0) {
			return exor.listWithLocatorRaw("", false)
		} else if (args.length == 1) {
			return exor.listWithLocatorRaw((String)args[0], false)
		} else {
			return exor.listWithLocatorRaw((String)args[0], (Boolean)args[1])
		}
	}

	//-------------------------------------------------------------------------
	static String listWithLocator(Object ... args) throws Exception {
		ExtendedObjectRepository exor = new ExtendedObjectRepository()
		if (args.length == 0) {
			return exor.listWithLocator("", false)
		} else if (args.length == 1) {
			return exor.listWithLocator((String)args[0], false)
		} else {
			return exor.listWithLocator((String)args[0], (Boolean)args[1])
		}
	}


	//-------------------------------------------------------------------------
	static Map<String, Set<String>> reverseLookupRaw(Object ... args)
	throws IOException {
		if (args.length == 0) {
			return doReverseLookupRaw("", false)
		} else if (args.length == 1) {
			return doReverseLookupRaw((String)args[0], false)
		} else {
			return doReverseLookupRaw((String)args[0], (Boolean)args[1])
		}
	}

	private static Map<String, Set<String>> doReverseLookupRaw(String pattern,
			Boolean isRegex)
	throws IOException {
		Map<String, Set<String>> result = new TreeMap<>()
		BilingualMatcher bim = new BilingualMatcher(pattern, isRegex)
		List<String> idList = listRaw()  // list of IDs of Test Object
		idList.forEach { id ->
			String locator = findLocator(id)
			Set<String> idSet
			if (result.containsKey(locator)) {
				idSet = result.get(locator)
			} else {
				idSet = new TreeSet<>()
			}
			if (bim.found(locator)) {
				idSet.add(id)
				result.put(locator, idSet)
			}
		}
		return result
	}

	//-------------------------------------------------------------------------
	static String reverseLookup(Object ... args) throws IOException {
		if (args.length == 0) {
			return doReverseLookup("", false)
		} else if (args.length == 1) {
			return doReverseLookup((String)args[0], false)
		} else {
			return doReverseLookup((String)args[0], (Boolean)args[1])
		}
	}

	private static doReverseLookup(String pattern, Boolean isRegex)
	throws IOException {
		Map<String, Set<String>> result = this.reverseLookupRaw(pattern, isRegex)
		String json = JsonOutput.toJson(result)
		return JsonOutput.prettyPrint(json)
	}

	//-------------------------------------------------------------------------
	// helpers

	private static Path getBaseDir() {
		Path projectDir = Paths.get(RunConfiguration.getProjectDir())
		return projectDir.resolve("Object Repository")
	}

	private static String findLocator(String testObjectId) {
		Objects.requireNonNull(testObjectId)
		TestObject tObj = ObjectRepository.findTestObject(testObjectId)
		SelectorMethod selectorMethod = tObj.getSelectorMethod()
		return tObj.getSelectorCollection().getAt(selectorMethod)
	}

}


