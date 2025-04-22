package com.kazurayam.ks.testobject

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver
import com.kazurayam.ks.testobject.TestObjectEssence.TestObjectEssenceSerializer
import com.kazurayam.ks.testobject.gc.Database
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.Files
import java.nio.file.Path

/**
 * ObjectRepositoryDecorator implements methods that returns a list of TestObjectId,
 * a list of TestObjectEssenses, that match with the pattern for Locators.
 * ObjectRepositoryDecorator enables to look up Test Objects by Locators.
 */
class ObjectRepositoryDecorator {

	private static final Logger logger = LoggerFactory.getLogger(ObjectRepositoryDecorator.class)

	private Path objectRepositoryDir
	private List<String> includeFolderSpecification
	private ObjectRepositoryAccessor accessor

	private ObjectRepositoryDecorator(Builder builder) {
		objectRepositoryDir = builder.objectRepositoryDir
		includeFolderSpecification = builder.includeFolder
		Objects.requireNonNull(this.objectRepositoryDir)
		Objects.requireNonNull(this.includeFolderSpecification)
		init()
	}

	private init() {
		// the following line is the whole reason why we need this class
		List<String> patternsForFile = translatePatterns(includeFolderSpecification)
		accessor =
				new ObjectRepositoryAccessor.Builder(objectRepositoryDir)
				.includeFiles(patternsForFile)
				.build()
	}

	Path getObjectRepositoryDir() {
		return objectRepositoryDir
	}

	List<String> getIncludeFolderSpecification() {
		return includeFolderSpecification
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
	 *
	 * returns a List of TestObjectEssense object, which comprises with "TestObjectId", "Method" and "Locator".
	 * You can select the target TestObjects to choose by the "pattern" and "isRegex" parameters
	 */
	List<TestObjectEssence> getTestObjectEssenceList(String pattern, Boolean isRegex = false) throws IOException {
		List<TestObjectId> ids = accessor.getTestObjectIdList()
		RegexOptedTextMatcher m = new RegexOptedTextMatcher(pattern, isRegex)
		//
		List<TestObjectEssence> result = new ArrayList<>()
		ids.forEach { id ->
			TestObject tObj = ObjectRepository.findTestObject(id.getValue())
			Locator locator = id.toTestObjectEssence().getLocator()
			if (tObj != null && m.found(id.getValue())) {
				TestObjectEssence essence =
						new TestObjectEssence(id, tObj.getSelectorMethod().toString(), locator)
				result.add(essence)
			}
		}
		return result
	}

	/**
	 * convert a pattern for Object Repository sub-folders to a pattern for TestObject files 
	 *　E.g, "** /Page_CURA*" -> "** /Page_CURA* /** /*.rs"
	 * @param includeFolderSpecification
	 * @return
	 */
	protected List<String> translatePatterns(List<String> patterns) {
		List<String> patternsForFile = new ArrayList<>()
		patterns.each { ptrn ->
			StringBuilder sb = new StringBuilder()
			sb.append(ptrn)
			if (!ptrn.endsWith("/")) {
				sb.append("/")
			}
			sb.append("**/*.rs")
			patternsForFile.add(sb.toString())
		}
		return patternsForFile
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
	 * @return a Set of all TestObjectIds contained in the "Object Repository"
	 */
	Set<TestObjectId> getAllTestObjectIdSet() {
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
	LocatorIndex getLocatorIndex(Database db, String pattern = "", Boolean isRegex = false) throws IOException {
		Objects.requireNonNull(db)
		LocatorIndex locatorIndex = new LocatorIndex()
		RegexOptedTextMatcher textMatcher = new RegexOptedTextMatcher(pattern, isRegex)
		List<TestObjectId> idList = this.getTestObjectIdList("", false)  // list of IDs of Test Object
		idList.forEach { id ->
			Locator locator = id.toTestObjectEssence().getLocator()
			if (textMatcher.found(locator.getValue())) {
				TestObjectEssence essence = id.toTestObjectEssence()
				int numberOfReferrers = db.findForwardReferencesTo(essence.getTestObjectId()).size()
				essence.setNumberOfReferrers(numberOfReferrers)
				locatorIndex.put(locator, essence)
			}
		}
		return locatorIndex
	}

	/**
	 * returns a JSON string representation of the LocatorIndex object that is returned by the "getLocatorIndex" call.
	 */
	String jsonifyLocatorIndex(Database db, String pattern = "", Boolean isRegex = false) throws IOException {
		LocatorIndex locatorIndex = this.getLocatorIndex(db, pattern, isRegex)
		return locatorIndex.toJson()
	}


	/**
	 * 
	 * @author kazurayam
	 */
	static class Builder {
		private Path objectRepositoryDir
		private List<String> includeFolder
		Builder() {
			this(KatalonProjectDirectoryResolver.getProjectDir().resolve("Object Repository"))
		}
		Builder(Path dir) {
			Objects.requireNonNull(dir)
			assert Files.exists(dir)
			objectRepositoryDir = dir
			includeFolder = new ArrayList<>()
		}
		Builder includeFolder(String pattern) {
			Objects.requireNonNull(pattern)
			this.includeFolder.add(pattern)
			return this
		}
		Builder includeFolder(List<String> pattern) {
			Objects.requireNonNull(pattern)
			this.includeFolder.addAll(pattern)
			return this
		}
		ObjectRepositoryDecorator build() {
			assert objectRepositoryDir != null : "objectRepositoryDir is left null"
			return new ObjectRepositoryDecorator(this)
		}
	}
}
