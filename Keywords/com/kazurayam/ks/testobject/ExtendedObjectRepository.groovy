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

/**
 * ExtendedObjectRepository wraps the so-called "Object Repository" directory,
 * implements various accessors for the contents in the directory.
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

	String listTestObjectId(String pattern = "", Boolean isRegex = false) throws IOException {
		List<TestObjectId> list = listTestObjectIdRaw(pattern, isRegex)
		StringBuilder sb = new StringBuilder()
		sb.append("{")
		sb.append(JsonOutput.toJson("ExtendedObjectRepository#listTestObjectId"))
		sb.append(":")
		sb.append("[")
		String sep = ""
		list.forEach { toi ->
			sb.append(sep)
			sb.append(toi.toJson())
			sep = ","
		}
		sb.append("]")
		sb.append("}")
		return JsonOutput.prettyPrint(sb.toString())
	}

	List<TestObjectId> listTestObjectIdRaw(String pattern = "", Boolean isRegex = false) throws IOException {
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

	String listEssence(String pattern, Boolean isRegex) throws IOException {
		List<TestObjectEssence> result = this.listEssenceRaw(pattern, isRegex)
		StringBuilder sb = new StringBuilder()
		sb.append("{")
		sb.append(JsonOutput.toJson("ExtendedObjectRepository#listEssence"))
		sb.append(":")
		sb.append("[")
		String sep = ""
		result.forEach { tog ->
			sb.append(sep)
			sb.append(tog.toJson())
			sep = ","
		}
		sb.append("]")
		sb.append("}")
		return JsonOutput.prettyPrint(sb.toString())
	}

	List<TestObjectEssence> listEssenceRaw(String pattern, Boolean isRegex) throws IOException {
		ObjectRepositoryVisitor visitor = new ObjectRepositoryVisitor(baseDir)
		Path dir = getTargetDir()
		Files.walkFileTree(dir, visitor)
		List<TestObjectId> ids = visitor.getTestObjectIdList()
		RegexOptedTextMatcher m = new RegexOptedTextMatcher(pattern, isRegex)
		//
		List<TestObjectEssence> result = new ArrayList<>()
		ids.forEach { id ->
			TestObject tObj = ObjectRepository.findTestObject(id.value())
			Locator locator = findLocator(id)
			if (m.found(id.value())) {
				TestObjectEssence gist =
						new TestObjectEssence(id, tObj.getSelectorMethod().toString(), locator)
				result.add(gist)
			}
		}
		return result
	}

	//-------------------------------------------------------------------------


	Map<Locator, Set<TestObjectEssence>> reverseLookupRaw(String pattern = "", Boolean isRegex = false) throws IOException {
		Map<Locator, Set<TestObjectEssence>> result = new TreeMap<>()
		RegexOptedTextMatcher m = new RegexOptedTextMatcher(pattern, isRegex)
		List<TestObjectId> idList = this.listTestObjectIdRaw("", false)  // list of IDs of Test Object
		idList.forEach { id ->
			Locator locator = findLocator(id)
			Set<TestObjectId> idSet
			if (result.containsKey(locator)) {
				idSet = result.get(locator)
			} else {
				idSet = new TreeSet<>()
			}
			if (m.found(locator.value())) {
				TestObjectEssence gist = findGist(id)
				idSet.add(gist)
				result.put(locator, idSet)
			}
		}
		return result
	}

	String reverseLookup(String pattern = "", Boolean isRegex = false) throws IOException {
		Map<Locator, Set<TestObjectEssence>> result = this.reverseLookupRaw(pattern, isRegex)
		StringBuilder sb = new StringBuilder()
		sb.append("{")
		sb.append(JsonOutput.toJson("ExtendedObjectRepository#reverseLookup"))
		sb.append(":")
		sb.append("[")
		String sep1 = ""
		result.keySet().forEach { locator ->
			sb.append(sep1)
			sb.append("{")
			sb.append(locator.toJson())
			sb.append(",")
			sb.append("[")
			Set<TestObjectEssence> gists = result.get(locator)
			String sep2 = ""
			gists.forEach { gist ->
				sb.append(sep2)
				sb.append(gist.toJson())
				sep2 = ","
			}
			sb.append("]")
			sb.append("}")
			sep1 = ","
		}
		sb.append("]")
		sb.append("}")
		return JsonOutput.prettyPrint(sb.toString())
	}

	//-------------------------------------------------------------------------

	private Locator findLocator(TestObjectId testObjectId) {
		Objects.requireNonNull(testObjectId)
		TestObjectEssence gist = findGist(testObjectId)
		return gist.locator()
	}

	private TestObjectEssence findGist(TestObjectId testObjectId) {
		Objects.requireNonNull(TestObjectId)
		TestObject tObj = ObjectRepository.findTestObject(testObjectId.value())
		assert tObj != null: "ObjectRepository.findTestObject('${testObjectId.value()}') returned null"
		SelectorMethod selectorMethod = tObj.getSelectorMethod()
		Locator locator = new Locator(tObj.getSelectorCollection().getAt(selectorMethod))
		TestObjectEssence gist = new TestObjectEssence(testObjectId,
				selectorMethod.toString(),
				locator)
		return gist
	}

	//-------------------------------------------------------------------------

	public Set<TestObjectId> getAllTestObjectIds() {
		Set<TestObjectId> result = new TreeSet<>()
		List<TestObjectEssence> allGist = listEssenceRaw("", false)
		allGist.forEach { gist ->
			TestObjectId toi = gist.testObjectId()
			if (toi != null && toi.value() != "") {
				result.add(toi)
			}
		}
		return result
	}
}
