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

	private Path objectRepositoryDir

	private ExtendedObjectRepository(Builder builder) {
		this.objectRepositoryDir = builder.objectRepositoryDir
	}

	Path getObjectRepositoryDir() {
		return objectRepositoryDir
	}

	/**
	 * @return a Set of all TestObjectIds contained in the "Object Repository"
	 */
	public Set<TestObjectId> getAllTestObjectIdSet() {
		Set<TestObjectId> result = new TreeSet<>()   // ordered set
		List<TestObjectEssence> allEssence = getTestObjectEssenceList("", false)
		allEssence.forEach { essence ->
			TestObjectId toi = essence.getTestObjectId()
			if (toi != null && toi.getValue() != "") {
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
		ObjectRepositoryAccessor accessor = new ObjectRepositoryAccessor(objectRepositoryDir)
		List<TestObjectId> ids = accessor.getTestObjectIdList()
		//
		List<TestObjectId> result = new ArrayList<>()
		RegexOptedTextMatcher m = new RegexOptedTextMatcher(pattern, isRegex)
		ids.forEach { testObjectId ->
			if (m.found(testObjectId.getValue().toString())) {
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
		ObjectRepositoryAccessor accessor = new ObjectRepositoryAccessor(objectRepositoryDir)
		List<TestObjectId> ids = accessor.getTestObjectIdList()
		RegexOptedTextMatcher m = new RegexOptedTextMatcher(pattern, isRegex)
		//
		List<TestObjectEssence> result = new ArrayList<>()
		ids.forEach { id ->
			TestObject tObj = ObjectRepository.findTestObject(id.getValue())
			Locator locator = id.toTestObjectEssence().getLocator()
			if (m.found(id.getValue())) {
				TestObjectEssence essence =
						new TestObjectEssence(id, tObj.getSelectorMethod().toString(), locator)
				result.add(essence)
			}
		}
		return result
	}

	String jsonifyTestObjectEssenceList(String pattern, Boolean isRegex) throws IOException {
		//
		ObjectMapper mapper = new ObjectMapper()
		SimpleModule module = new SimpleModule("jsonifyTestObjectEssenceList",
				new Version(1, 0, 0, null, null, null))
		module.addSerializer(TestObjectId.class, new TestObjectId.TestObjectIdSerializer())
		module.addSerializer(TestObjectEssence.class, new TestObjectEssenceSerializer())
		module.addSerializer(Locator.class, new Locator.LocatorSerializer())
		mapper.registerModule(module)
		//
		List<TestObjectEssence> list = getTestObjectEssenceList(pattern, isRegex)
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
			Locator locator = id.toTestObjectEssence().getLocator()
			if (textMatcher.found(locator.getValue())) {
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



	public static class Builder {
		private Path objectRepositoryDir
		public Builder() {
			this(Paths.get(".").resolve("Object Repository"))
		}
		public Builder(Path dir) {
			Objects.requireNonNull(dir)
			assert Files.exists(dir)
			objectRepositoryDir = dir
		}
		public ExtendedObjectRepository build() {
			return new ExtendedObjectRepository(this)
		}
	}
}
