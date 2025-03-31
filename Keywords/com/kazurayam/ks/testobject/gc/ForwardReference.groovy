package com.kazurayam.ks.testobject.gc

import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testcase.DigestedLine
import com.kazurayam.ks.testobject.TestObjectEssence

import groovy.json.JsonOutput

/**
 * A ForwardReference object records a fact that a single TestCase refers to a single TestObject. 
 */
public class ForwardReference implements Comparable<ForwardReference> {

	private TestCaseId testCaseId
	private DigestedLine digestedLine
	private TestObjectEssence testObjectEssence

	ForwardReference(TestCaseId testCaseId, DigestedLine digestedLine, TestObjectEssence testObjectEssence) {
		Objects.requireNonNull(testCaseId)
		Objects.requireNonNull(digestedLine)
		Objects.requireNonNull(testObjectEssence)
		this.testCaseId = testCaseId
		this.digestedLine = digestedLine
		this.testObjectEssence = testObjectEssence
	}

	TestCaseId testCaseId() {
		return testCaseId
	}

	DigestedLine textSearchResult() {
		return digestedLine
	}

	TestObjectEssence testObjectEssence() {
		return testObjectEssence
	}

	@Override
	boolean equals(Object obj) {
		if (!(obj instanceof ForwardReference)) {
			return false
		}
		ForwardReference other = (ForwardReference)obj
		return this.testCaseId == other.testCaseId &&
				this.digestedLine == other.digestedLine &&
				this.testObjectEssence == other.testObjectEssence
	}

	@Override
	int hashCode() {
		int hash = 7;
		hash = 31 * hash + testCaseId.hashCode()
		hash = 31 * hash + digestedLine.hashCode()
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
		sb.append(JsonOutput.toJson("ForwardReference"))
		sb.append(":")
		sb.append("{")
		sb.append(JsonOutput.toJson("TestCaseId"))
		sb.append(":")
		sb.append(JsonOutput.toJson(testCaseId.value()))
		sb.append(",")
		sb.append(JsonOutput.toJson("TextSearchResult"))
		sb.append(":")
		sb.append(digestedLine.toJson())
		sb.append(",")
		sb.append(JsonOutput.toJson("TestObjectEssence"))
		sb.append(":")
		sb.append(testObjectEssence.toJson())
		sb.append("}")
		sb.append("}")
		return JsonOutput.prettyPrint(sb.toString())
	}

	@Override
	int compareTo(ForwardReference other) {
		int v = this.testCaseId.compareTo(other.testCaseId)
		if (v != 0) {
			return v
		} else {
			v = this.digestedLine.compareTo(other.digestedLine)
			if (v != 0) {
				return v
			} else {
				return this.testObjectEssence.compareTo(other.testObjectEssence)
			}
		}
	}
}
