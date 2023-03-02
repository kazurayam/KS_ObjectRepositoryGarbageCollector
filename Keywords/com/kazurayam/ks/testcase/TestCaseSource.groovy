package com.kazurayam.ks.testcase

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
import java.util.regex.Pattern
import java.util.regex.Matcher

public class TestCaseSource {

	private List<String> lines = new ArrayList<>()

	public TestCaseSource(String code) {
		Objects.requireNonNull(code)
		this.lines = toLines(code)
	}

	public TestCaseSource(Path file) throws IOException {
		Objects.requireNonNull(file)
		this.lines = toLines(file.toFile().text)
	}

	public TestCaseSource(File file) throws IOException {
		Objects.requireNonNull(file)
		this.lines = toLines(file.text)
	}
	
	private List<String> toLines(String text) {
		BufferedReader br = new BufferedReader(new StringReader(text))
		List<String> lines = new ArrayList<>()
		String line
		while ((line = br.readLine()) != null) {
			lines.add(line)
		}
		return lines		
	}
	
	public List<TextSearchResult> searchText(String pattern, Boolean isRegex) {
		List<TextSearchResult> result = new ArrayList<>()
		if (pattern.length() == 0) {
			return result    // no search will be performed; return an empty list
		}
		Pattern ptrn = null
		if (isRegex) {
			ptrn = Pattern.compile("(.*)(" + pattern + ")(.*)")
		}
		// iterate over lines to find the matches
		for (int i = 0; i < lines.size(); i++) {
			String line = lines[i]
			int lineNo = i + 1
			// will lookup at most 1 match per line
			// 2nd and later matches, if any, will be ignored --- for easier implementation
			if (isRegex) {
				Matcher m = ptrn.matcher(line)
				if (m.find()) {
					String head = m.group(1)
					String matched = m.group(2)
					int matchAt = head.length() + 1
					int matchEnd = head.length() + matched.length()
					TextSearchResult tsr = new TextSearchResult.Builder(line, lineNo)
											.pattern(pattern, isRegex)
											.matchFound(matchAt, matchEnd)
											.build()
					result.add(tsr)
				}
			} else {
				if (line.indexOf(pattern) > 0) {
					int matchAt = line.indexOf(pattern) + 1
					int matchEnd = matchAt +  pattern.length()
					TextSearchResult tsr = new TextSearchResult.Builder(line, lineNo)
											.pattern(pattern, isRegex)
											.matchFound(matchAt, matchEnd)
											.build()
					result.add(tsr)
				}
			}
		}
		return result
	}
}
