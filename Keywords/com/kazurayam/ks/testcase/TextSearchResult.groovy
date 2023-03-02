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

public class TextSearchResult {
	
	private String line
	private int lineNo
	//
	private String pattern
	private Boolean isRegex
	//
	private int matchAt
	private int matchEnd
	private Boolean hasMatch

	private TextSearchResult(Builder builder) {
		this.line = builder.line
		this.lineNo = builder.lineNo
		this.pattern = builder.pattern
		this.isRegex = builder.isRegex
		this.matchAt = builder.matchAt
		this.matchEnd = builder.matchEnd
		this.hasMatch = builder.hasMatch
	}
	
	public String line() {
		return this.line
	}
	
	public int lineNo() {
		return this.lineNo
	}
	
	public String pattern() {
		return this.pattern
	}
	
	public Boolean isRegex() {
		return this.isRegex
	}
	
	public int matchAt() {
		return this.matchAt
	}
	
	public int matchEnd() {
		return this.matchEnd
	}
	
	public Boolean hasMatch() {
		return this.hasMatch
	}
	
	public static class Builder {
		String line
		int lineNo
		//
		String pattern
		Boolean isRegex
		//
		int matchAt
		int matchEnd
		Boolean hasMatch
		
		/**
		 * @param line
		 * @param lineNo 1,2,3,4,...
		 */
		Builder(String line, int lineNo) {
			this.line = line
			this.lineNo = lineNo
			this.pattern = ""
			this.isRegex = false
			this.matchAt = 0
			this.matchEnd = 0
			this.hasMatch = false
		}
		
		Builder pattern(String pattern, Boolean isRegex) {
			Objects.requireNonNull(pattern)
			this.pattern = pattern
			this.isRegex = isRegex
			return this
		}
		
		/**
		 * @param matchAt 1,2,3,4,...
		 * @param matchEnd 1,2,3,4,...
		 */
		Builder matchFound(int matchAt, int matchEnd) {
			assert matchAt > 0
			assert matchEnd > 0
			assert matchAt < matchEnd
			this.matchAt = matchAt
			this.matchEnd = matchEnd
			this.hasMatch = true
			return this
		}
		
		TextSearchResult build() {
			return new TextSearchResult(this)
		}
	}
}
