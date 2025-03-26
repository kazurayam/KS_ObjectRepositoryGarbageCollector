package com.kazurayam.ks.testobject.gc

import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testcase.TextSearchResult
import com.kazurayam.ks.testobject.TestObjectEssence

import groovy.json.JsonOutput

/**
 * A TCTOReference object records a fact that a TestCase refers to a TestObject. 
 */
public class TCTOReference implements Comparable<TCTOReference> {

	private TestCaseId testCaseId
	private TextSearchResult textSearchResult
	private TestObjectEssence testObjectEssence

	TCTOReference(TestCaseId testCaseId, TextSearchResult textSearchResult, TestObjectEssence testObjectEssence) {
		this.testCaseId = testCaseId
		this.textSearchResult = textSearchResult
		this.testObjectEssence = testObjectEssence
	}

	TestCaseId testCaseId() {
		return testCaseId
	}

	TextSearchResult textSearchResult() {
		return textSearchResult
	}

	TestObjectEssence testObjectEssence() {
		return testObjectEssence
	}

	@Override
	boolean equals(Object obj) {
		if (!(obj instanceof TCTOReference)) {
			return false
		}
		TCTOReference other = (TCTOReference)obj
		return this.testCaseId == other.testCaseId &&
				this.textSearchResult == other.textSearchResult &&
				this.testObjectEssence == other.testObjectEssence
	}

	@Override
	int hashCode() {
		int hash = 7;
		hash = 31 * hash + testCaseId.hashCode()
		hash = 31 * hash + textSearchResult.hashCode()
		hash = 31 * hash + testObjectEssence.hashCode()
		return hash;
	}

	@Override
	String toString() {
		return toJson()
	}

	String toJson() {
		StringBuilder sb = new StringBuilder()
		sb.append("{")
		sb.append(JsonOutput.toJson("TCTOReference"))
		sb.append(":")
		sb.append("{")
		sb.append(JsonOutput.toJson("TestCaseId"))
		sb.append(":")
		sb.append(JsonOutput.toJson(testCaseId.value()))
		sb.append(",")
		sb.append(JsonOutput.toJson("TextSearchResult"))
		sb.append(":")
		sb.append(textSearchResult.valueAsJson())
		sb.append(",")
		sb.append(JsonOutput.toJson("TestObjectEssence"))
		sb.append(":")
		sb.append(testObjectEssence.valueAsJson())
		sb.append("}")
		sb.append("}")
		return JsonOutput.prettyPrint(sb.toString())
	}

	@Override
	int compareTo(TCTOReference other) {
		int v = this.testCaseId.compareTo(other.testCaseId)
		if (v != 0) {
			return v
		} else {
			v = this.textSearchResult.compareTo(other.textSearchResult)
			if (v != 0) {
				return v
			} else {
				return this.testObjectEssence.compareTo(other.testObjectEssence)
			}
		}
	}
}
