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
			logger.debug("pattern=${pattern}, isRegex=${isRegex}, id=${id.value()}, bim.found(id)=${bim.found(id.value())}")
			if (bim.found(id.toString())) {
				result.add(id)
			}
		}
		return result;
	}

	String listGist(String pattern, Boolean isRegex) throws IOException {
		List<TestObjectGist> result = this.listGistRaw(pattern, isRegex)
		String json = JsonOutput.toJson(result)
		return JsonOutput.prettyPrint(json)
	}

	List<TestObjectGist> listGistRaw(String pattern, Boolean isRegex) throws IOException {
		Path dir = getBaseDir()
		ObjectRepositoryVisitor visitor = new ObjectRepositoryVisitor(dir)
		Files.walkFileTree(dir, visitor)
		List<TestObjectId> ids = visitor.getTestObjectIdList()
		BilingualMatcher bim = new BilingualMatcher(pattern, isRegex)
		//
		List<TestObjectGist> result = new ArrayList<>()
		ids.forEach { id ->
			TestObject tObj = ObjectRepository.findTestObject(id.value())
			Locator locator = findLocator(id)
			if (bim.found(id.value())) {
				TestObjectGist gist =
						new TestObjectGist(id, tObj.getSelectorMethod().toString(), locator)
				result.add(gist)
			}
		}
		return result
	}

	//-------------------------------------------------------------------------


	Map<Locator, Set<TestObjectGist>> reverseLookupRaw(String pattern, Boolean isRegex) throws IOException {
		Map<Locator, Set<TestObjectGist>> result = new TreeMap<>()
		BilingualMatcher bim = new BilingualMatcher(pattern, isRegex)
		List<TestObjectId> idList = this.listRaw("", false)  // list of IDs of Test Object
		idList.forEach { id ->
			Locator locator = findLocator(id)
			Set<TestObjectId> idSet
			if (result.containsKey(locator)) {
				idSet = result.get(locator)
			} else {
				idSet = new TreeSet<>()
			}
			if (bim.found(locator.value())) {
				TestObjectGist gist = findGist(id)
				idSet.add(gist)
				result.put(locator, idSet)
			}
		}
		return result
	}

	String reverseLookup(String pattern, Boolean isRegex) throws IOException {
		Map<Locator, Set<TestObjectGist>> result = this.reverseLookupRaw(pattern, isRegex)
		String json = JsonOutput.toJson(result)
		return JsonOutput.prettyPrint(json)
	}

	//-------------------------------------------------------------------------

	private Locator findLocator(TestObjectId testObjectId) {
		Objects.requireNonNull(testObjectId)
		TestObjectGist gist = findGist(testObjectId)
		return gist.locator()
	}

	private TestObjectGist findGist(TestObjectId testObjectId) {
		Objects.requireNonNull(TestObjectId)
		TestObject tObj = ObjectRepository.findTestObject(testObjectId.value())
		SelectorMethod selectorMethod = tObj.getSelectorMethod()
		Locator locator = new Locator(tObj.getSelectorCollection().getAt(selectorMethod))
		TestObjectGist gist = new TestObjectGist(testObjectId,
				selectorMethod.toString(),
				locator)
		return gist
	}

}
