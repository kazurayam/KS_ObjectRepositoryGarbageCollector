package com.kazurayam.ks.testobject

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject

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

	String jsonifyTestObjectIdList(String pattern = "", Boolean isRegex = false) throws IOException {
		List<TestObjectId> list = getTestObjectIdList(pattern, isRegex)
		StringBuilder sb = new StringBuilder()
		sb.append("{")
		sb.append(JsonOutput.toJson("ExtendedObjectRepository#TestObjectIdList"))
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
			Locator locator = findLocator(id)
			if (m.found(id.value())) {
				TestObjectEssence essence =
						new TestObjectEssence(id, tObj.getSelectorMethod().toString(), locator)
				result.add(essence)
			}
		}
		return result
	}

	String jsonifyTestObjectEssenceList(String pattern, Boolean isRegex) throws IOException {
		List<TestObjectEssence> result = getTestObjectEssenceList(pattern, isRegex)
		StringBuilder sb = new StringBuilder()
		sb.append("{")
		sb.append(JsonOutput.toJson("ExtendedObjectRepository#TestObjectEssenceList"))
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

	
	//-------------------------------------------------------------------------


	Map<Locator, Set<TestObjectEssence>> getBackwardReferences(String pattern = "", Boolean isRegex = false) throws IOException {
		Map<Locator, Set<TestObjectEssence>> result = new TreeMap<>()
		RegexOptedTextMatcher m = new RegexOptedTextMatcher(pattern, isRegex)
		List<TestObjectId> idList = getTestObjectIdList("", false)  // list of IDs of Test Object
		idList.forEach { id ->
			Locator locator = findLocator(id)
			Set<TestObjectId> idSet
			if (result.containsKey(locator)) {
				idSet = result.get(locator)
			} else {
				idSet = new TreeSet<>()
			}
			if (m.found(locator.value())) {
				TestObjectEssence essence = getTestObjectEssenceList(id, isRegex)
				idSet.add(essence)
				result.put(locator, idSet)
			}
		}
		return result
	}

	String jsonifyBackwardReferences(String pattern = "", Boolean isRegex = false) throws IOException {
		Map<Locator, Set<TestObjectEssence>> result = getBackwardReferences(pattern, isRegex)
		StringBuilder sb = new StringBuilder()
		sb.append("{")
		sb.append(JsonOutput.toJson("ExtendedObjectRepository#backwardReferences"))
		sb.append(":")
		sb.append("[")
		String sep1 = ""
		result.keySet().forEach { locator ->
			sb.append(sep1)
			sb.append("{")
			sb.append(locator.toJson())
			sb.append(",")
			sb.append("[")
			Set<TestObjectEssence> essences = result.get(locator)
			String sep2 = ""
			essences.forEach { essence ->
				sb.append(sep2)
				sb.append(essence.toJson())
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
		TestObjectEssence essence= testObjectId.toTestObjectEssence()
		return essence.locator()
	}

	//-------------------------------------------------------------------------

	public Set<TestObjectId> getAllTestObjectIds() {
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
}
