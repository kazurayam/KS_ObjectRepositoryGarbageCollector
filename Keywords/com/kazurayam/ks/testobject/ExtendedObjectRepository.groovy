package com.kazurayam.ks.testobject

import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import com.kms.katalon.core.testobject.SelectorMethod

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import groovy.json.JsonOutput

public class ExtendedObjectRepository {

	private static Logger logger = LoggerFactory.getLogger(ExtendedObjectRepository.class)

	private Path baseDir

	ExtendedObjectRepository() {
		this(Paths.get(".").resolve("Object Repository"))
	}

	ExtendedObjectRepository(Path baseDir) {
		Objects.requireNonNull(baseDir)
		assert Files.exists(baseDir)
		this.baseDir = baseDir
	}

	Path getBaseDir() {
		return baseDir
	}

	String list(String pattern, Boolean isRegex) throws IOException {
		List<String> list = listRaw(pattern, isRegex)
		String json = JsonOutput.toJson(list)
		String pp = JsonOutput.prettyPrint(json)
		return pp
	}

	List<String> listRaw(String pattern, Boolean isRegex) throws IOException {
		Path dir = getBaseDir()
		ObjectRepositoryVisitor visitor = new ObjectRepositoryVisitor(dir)
		Files.walkFileTree(dir, visitor)
		List<String> ids = visitor.getTestObjectIdList()
		//
		List<String> result = new ArrayList<>()
		BilingualMatcher bim = new BilingualMatcher(pattern, isRegex)
		ids.forEach { id ->
			logger.debug("pattern=${pattern}, isRegex=${isRegex}, id=${id}, bim.found(id)=${bim.found(id)}")
			if (bim.found(id)) {
				result.add(id)
			}
		}
		return result;
	}

	String listWithLocator(String pattern, Boolean isRegex) throws IOException {
		List<Map<String, String>> result = this.listWithLocatorRaw(pattern, isRegex)
		String json = JsonOutput.toJson(result)
		return JsonOutput.prettyPrint(json)
	}

	List<Map<String, String>> listWithLocatorRaw(String pattern, Boolean isRegex) throws IOException {
		Path dir = getBaseDir()
		ObjectRepositoryVisitor visitor = new ObjectRepositoryVisitor(dir)
		Files.walkFileTree(dir, visitor)
		List<String> ids = visitor.getTestObjectIdList()
		BilingualMatcher bim = new BilingualMatcher(pattern, isRegex)
		//
		List<Map<String, String>> result = new ArrayList<>()
		ids.forEach { id ->
			TestObject tObj = ObjectRepository.findTestObject(id)
			String locator = findLocator(id)
			if (bim.found(id)) {
				Map<String, String> entry = new LinkedHashMap<>()
				entry.put("id", id)
				entry.put("method", tObj.getSelectorMethod().toString())
				entry.put("locator", locator)
				result.add(entry)
			}
		}
		return result
	}

	//-------------------------------------------------------------------------


	Map<String, Set<String>> reverseLookupRaw(String pattern, Boolean isRegex) throws IOException {
		Map<String, Set<String>> result = new TreeMap<>()
		BilingualMatcher bim = new BilingualMatcher(pattern, isRegex)
		List<String> idList = this.listRaw("", false)  // list of IDs of Test Object
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

	String reverseLookup(String pattern, Boolean isRegex) throws IOException {
		Map<String, Set<String>> result = this.reverseLookupRaw(pattern, isRegex)
		String json = JsonOutput.toJson(result)
		return JsonOutput.prettyPrint(json)
	}

	//-------------------------------------------------------------------------

	private String findLocator(String testObjectId) {
		Objects.requireNonNull(testObjectId)
		TestObject tObj = ObjectRepository.findTestObject(testObjectId)
		SelectorMethod selectorMethod = tObj.getSelectorMethod()
		return tObj.getSelectorCollection().getAt(selectorMethod)
	}

}
