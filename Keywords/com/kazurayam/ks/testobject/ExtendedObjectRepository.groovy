package com.kazurayam.ks.testobject

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files
import groovy.json.JsonOutput

public class ExtendedObjectRepository {

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
		List<String> ids = visitor.getTestObjectIDs()
		//
		List<String> result = new ArrayList<>()
		BilingualMatcher bim = new BilingualMatcher(pattern, isRegex)
		ids.forEach { id ->
			if (bim.found(id)) {
				result.add(id)
			}
		}
		return result;
	}

}
