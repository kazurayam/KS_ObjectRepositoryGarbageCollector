package com.kazurayam.ks.testobject

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kazurayam.ks.testobject.TestObjectEssence.TestObjectEssenceSerializer
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject

import groovy.json.JsonOutput

/**
 * ExtendedObjectRepository wraps the so-called "Object Repository" directory,
 * implements various accessor methods for the Test Objects stored in the directory.
 *
 */
public class ExtendedObjectRepository {

	private static Logger logger = LoggerFactory.getLogger(ExtendedObjectRepository.class)

	private Path baseDir
	private String subpath = null

	ExtendedObjectRepository() {
		this(Paths.get(".").resolve("Object Repository"), null)
	}

	ExtendedObjectRepository(Path baseDir) {
		this(baseDir, null)
	}

	ExtendedObjectRepository(Path baseDir, String subpath) {
		Objects.requireNonNull(baseDir)
		assert Files.exists(baseDir)
		this.baseDir = baseDir
		this.subpath = subpath
	}

	Path getTargetDir() {
		return (subpath != null) ? baseDir.resolve(subpath) : baseDir
	}
	
	/**
	 * @return a Set of all TestObjectIds contained in the "Object Repository"
	 */
	public Set<TestObjectId> getAllTestObjectIdSet() {
		Set<TestObjectId> result = new TreeSet<>()   // ordered set
		List<TestObjectEssence> allEssence = getTestObjectEssenceList("", false)
		allEssence.forEach { essence ->
			TestObjectId toi = essence.testObjectId()
			if (toi != null && toi.value() != "") {
				result.add(toi)
			}
		}
		return result
	}

	/**
	 * 
	 * @param pattern e.g. "button_"
	 * @param isRegex
	 * @return a List of TestObjectIds of which id string matches the pattern. 
	 * The "pattern" could either be a plain string or a Regular Expression. 
	 * You can specify the interpretation by the "isRegex" parameter
	 * @throws IOException
	 */
	List<TestObjectId> getTestObjectIdList(String pattern = "", Boolean isRegex = false) throws IOException {
		Path dir = getTargetDir()
		ObjectRepositoryVisitor visitor = new ObjectRepositoryVisitor(baseDir)
		Files.walkFileTree(dir, visitor)
		List<TestObjectId> ids = visitor.getTestObjectIdList()
		//
		List<TestObjectId> result = new ArrayList<>()
		RegexOptedTextMatcher m = new RegexOptedTextMatcher(pattern, isRegex)
		ids.forEach { testObjectId ->
			if (m.found(testObjectId.value().toString())) {
				result.add(testObjectId)
			}
		}
		return result;
	}

	/**
	 * returns a JSON string representation of the call to "getTestObjectIdList"
	 */
	String jsonifyTestObjectIdList(String pattern = "", Boolean isRegex = false) throws IOException {
		List<TestObjectId> list = getTestObjectIdList(pattern, isRegex)
		//
		ObjectMapper mapper = new ObjectMapper()
		SimpleModule module = new SimpleModule("jsonifyTestObjectIdList",
				new Version(1, 0, 0, null, null, null))
		module.addSerializer(TestObjectId.class, new TestObjectId.TestObjectIdSerializer())
		module.addSerializer(TestObjectEssence.class, new TestObjectEssenceSerializer())
		module.addSerializer(Locator.class, new Locator.LocatorSerializer())
		mapper.registerModule(module)
		//
		return mapper.writeValueAsString(list)
	}

	/**
	 * 
	 * returns a List of TestObjectEssense object, which comprises with "TestObjectId", "Method" and "Locator".
	 * You can select the target TestObjects to choose by the "pattern" and "isRegex" parameters
	 */
	List<TestObjectEssence> getTestObjectEssenceList(String pattern, Boolean isRegex = false) throws IOException {
		ObjectRepositoryVisitor visitor = new ObjectRepositoryVisitor(baseDir)
		Path dir = getTargetDir()
		Files.walkFileTree(dir, visitor)
		List<TestObjectId> ids = visitor.getTestObjectIdList()
		RegexOptedTextMatcher m = new RegexOptedTextMatcher(pattern, isRegex)
		//
		List<TestObjectEssence> result = new ArrayList<>()
		ids.forEach { id ->
			TestObject tObj = ObjectRepository.findTestObject(id.value())
			Locator locator = id.toTestObjectEssence().locator()
			if (m.found(id.value())) {
				TestObjectEssence essence =
						new TestObjectEssence(id, tObj.getSelectorMethod().toString(), locator)
				result.add(essence)
			}
		}
		return result
	}

	String jsonifyTestObjectEssenceList(String pattern, Boolean isRegex) throws IOException {
		List<TestObjectEssence> list = getTestObjectEssenceList(pattern, isRegex)
		//
		ObjectMapper mapper = new ObjectMapper()
		SimpleModule module = new SimpleModule("jsonifyTestObjectEssenceList",
				new Version(1, 0, 0, null, null, null))
		module.addSerializer(TestObjectId.class, new TestObjectId.TestObjectIdSerializer())
		module.addSerializer(TestObjectEssence.class, new TestObjectEssenceSerializer())
		module.addSerializer(Locator.class, new Locator.LocatorSerializer())
		mapper.registerModule(module)
		//
		return mapper.writeValueAsString(list)
	}

	//-------------------------------------------------------------------------

	/**
	 * LocatorIndex is a list of "Locators", each of which associated with 
	 * the list of TestObjectEssence objects which have the same "Locator" string.
	 * 
	 * You should pay attention to the locators that has 2 or more belonging TestObjectEssence objects;
	 * as it means you have duplicating TestObjects with the same Locator.
	 */
	LocatorIndex getLocatorIndex(String pattern = "", Boolean isRegex = false) throws IOException {
		LocatorIndex locatorIndex = new LocatorIndex()
		RegexOptedTextMatcher textMatcher = new RegexOptedTextMatcher(pattern, isRegex)
		List<TestObjectId> idList = this.getTestObjectIdList("", false)  // list of IDs of Test Object
		idList.forEach { id ->
			Locator locator = id.toTestObjectEssence().locator()
			if (textMatcher.found(locator.value())) {
				TestObjectEssence essence = id.toTestObjectEssence()
				locatorIndex.put(locator, essence)
			}
		}
		return locatorIndex
	}

	/**
	 * returns a JSON string representation of the LocatorIndex object that is returned by the "getLocatorIndex" call.
	 */
	String jsonifyLocatorIndex(String pattern = "", Boolean isRegex = false) throws IOException {
		LocatorIndex locatorIndex = this.getLocatorIndex(pattern, isRegex)
		return locatorIndex.toJson()
	}
}
