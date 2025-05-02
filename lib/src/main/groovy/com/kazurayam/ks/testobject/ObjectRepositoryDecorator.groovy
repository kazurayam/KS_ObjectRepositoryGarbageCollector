package com.kazurayam.ks.testobject

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.kazurayam.ant.DirectoryScanner
import com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver
import com.kazurayam.ks.text.RegexOptedTextMatcher
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.SelectorMethod as KsSelectorMethod
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
		List<TestObjectId> toiList = getTestObjectIdList("", false)
		toiList.forEach { toi ->
			if (toi != null && toi.getValue() != "") {
				result.add(toi)
			}
		}
		return result
	}


	//-------------------------------------------------------------------------
	Set<Locator> getAllLocators() {
		Set<Locator> locators = new TreeSet<>()
		Set<TestObjectId> toiSet = getAllTestObjectIdSet()
		toiSet.each {toi ->
			Locator locator = getLocator(toi)
			locators.add(locator)
		}
		return locators
	}

	/**
	 * Returns an instance of Locator that corresponds to the given TestObjectId.
	 * This method depends on the Katalon API for
	 * com.kms.katalon.core.testobject.ObjectRepository and
	 * com.ksm.katalon.core.testobject.TestObject
	 *
	 * @param testObjectId
	 * @return the Locator object that corresponds to the given TestObjectId
	 */
	static Locator getLocator(TestObjectId testObjectId) {
		TestObject tObj = ObjectRepository.findTestObject(testObjectId.getValue())
		if (tObj != null) {
			KsSelectorMethod method = tObj.getSelectorMethod()
			String locatorValue = tObj.getSelectorCollection().getAt(method)
			return new Locator(locatorValue, SelectorMethod.valueOf(method.name()))
		} else {
			return Locator.NULL_OBJECT
		}
	}

	//

	/**
	 * LocatorIndex is a list of "Locators", each of which associated with
	 * the list of TestObjectEssence objects which have the same "Locator" string.
	 *
	 * You should pay attention to the locators that has 2 or more belonging TestObjectEssence objects;
	 * as it means you have duplicating TestObjects with the same Locator.
	 */
	LocatorIndex getLocatorIndex() throws IOException {
		Set<Locator> locatorSet = new TreeSet<>()
		List<TestObjectId> idList = this.getTestObjectIdList()  // list of IDs of Test Object
		idList.each { toi ->
			Locator locator = getLocator(toi)
			locatorSet.add(locator)
		}
		LocatorIndex locatorIndex = new LocatorIndex()
		locatorSet.each { locator ->
			Set<TestObjectId> testObjectIds = this.findTestObjectsWithLocator(locator)
			LocatorDeclarations declarations = new LocatorDeclarations(locator, testObjectIds)
			locatorIndex.put(locator, declarations)
		}
		return locatorIndex
	}

	Set<TestObjectId> findTestObjectsWithLocator(Locator locator) {
		Set<TestObjectId> testObjectsWithTheLocator = new TreeSet<>()
		this.getTestObjectIdList().each { toi ->
			if (locator == getLocator(toi)) {
				testObjectsWithTheLocator.add(toi)
			}
		}
		return testObjectsWithTheLocator
	}

	/**
	 * returns a JSON string representation of the LocatorIndex object that is returned by the "getLocatorIndex" call.
	 */
	String jsonifyLocatorIndex() throws IOException {
		LocatorIndex locatorIndex = this.getLocatorIndex()
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
	/**
	 * ObjectRepositoryAccessor requires the path of "Object Repository" folder in a
	 * Katalon project. Optionally it accepts Ant-like patterns that represents
	 * the sub-folders of "Object Repository" to be included.
	 */
	static class ObjectRepositoryAccessor {

		private static Logger logger = LoggerFactory.getLogger(ObjectRepositoryAccessor.class)

		private Path objectRepositoryDir
		private List<String> includeFilesSpecification
		private DirectoryScanner ds

		private ObjectRepositoryAccessor(Builder builder) {
			this.objectRepositoryDir = builder.objectRepositoryDir
			this.includeFilesSpecification = builder.includeFiles
			init()
		}

		private void init() {
			ds = new DirectoryScanner()
			ds.setBasedir(objectRepositoryDir.toFile())
			if (includeFilesSpecification.size() > 0) {
				String[] includes = includeFilesSpecification.toArray(new String[0])
				ds.setIncludes(includes)
			}
			ds.scan()
		}

		String[] getIncludedFiles() {
			return ds.getIncludedFiles()
		}

		List<TestObjectId> getTestObjectIdList() {
			String[] includedFiles = getIncludedFiles()
			List<TestObjectId> result = new ArrayList<>()
			for (int i = 0; i < includedFiles.length; i++) {
				if (includedFiles[i].endsWith(".rs")) {
					String filePath = includedFiles[i].replaceAll('\\.rs$', '')
					String idString = filePath.replace("\\", "/")
					TestObjectId toi = new TestObjectId(idString)
					result.add(toi)
				} else {
					logger.warn("found a file that does not end with '.rs'; ${includedFiles[i]}")
				}
			}
			return result
		}

		List<Path> getRsFiles() {
			String[] includedFiles = getIncludedFiles()
			List<Path> result = new ArrayList<>()
			for (int i = 0; i < includedFiles.length; i++) {
				if (includedFiles[i].endsWith(".rs")) {
					Path rs = objectRepositoryDir.resolve(includedFiles[i])
							.toAbsolutePath().normalize()
					result.add(rs)
				} else {
					logger.warn("found a file that does not end with '.rs'; ${includedFiles[i]}")
				}
			}
			return result
		}


		/**
		 *
		 * @author kazurayam
		 */
		static class Builder {
			private Path objectRepositoryDir
			private List<String> includeFiles
			Builder(Path orDir) {
				objectRepositoryDir = orDir.toAbsolutePath().normalize()
				includeFiles = new ArrayList<>()
			}

			ObjectRepositoryAccessor.Builder includeFile(String pattern) {
				Objects.requireNonNull(pattern)
				includeFiles.add(pattern)
				return this
			}

			ObjectRepositoryAccessor.Builder includeFiles(List<String> patterns) {
				Objects.requireNonNull(patterns)
				includeFiles.addAll(patterns)
				return this
			}
			ObjectRepositoryAccessor build() {
				return new ObjectRepositoryAccessor(this)
			}
		}
	}
}
