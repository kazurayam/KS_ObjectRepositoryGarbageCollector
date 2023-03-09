package com.kazurayam.ks.testobject

import groovy.json.JsonOutput

public class TestObjectGist implements Comparable<TestObjectGist> {

	private TestObjectId testObjectId       // "Page_CURA Healthcare Service/a_Go to Homepage"
	private String method   // "BASIC", "XPATH", "CSS"
	private Locator locator  // "//section[@id='summary']/div/div/div[7]/p/a"

	TestObjectGist(TestObjectId testObjectId, String method, Locator locator) {
		Objects.requireNonNull(testObjectId)
		Objects.requireNonNull(method)
		Objects.requireNonNull(locator)
		this.testObjectId = testObjectId
		this.method = method
		this.locator = locator
	}

	TestObjectId testObjectId() {
		return testObjectId
	}

	String method() {
		return method
	}

	Locator locator() {
		return locator
	}

	@Override
	boolean equals(Object obj) {
		if (!(obj instanceof TestObjectGist)) {
			return false
		}
		TestObjectGist other = (TestObjectGist)obj
		return this.testObjectId == other.testObjectId &&
				this.method == other.method &&
				this.locator == other.locator
	}

	@Override
	int hashCode() {
		int hash = 7;
		hash = 31 * hash + testObjectId.hashCode();
		hash = 31 * hash + method.hashCode()
		hash = 31 * hash + locator.hashCode()
		return hash;
	}

	@Override
	String toString() {
		return this.toJson()
	}

	String toJson() {
		StringBuilder sb = new StringBuilder()
		sb.append("{")
		sb.append("\"testObjectId\":")
		sb.append(JsonOutput.toJson(testObjectId.value()))
		sb.append(",")
		sb.append("\"method\":")
		sb.append(JsonOutput.toJson(method))
		sb.append(",")
		sb.append("\"locator\":")
		sb.append(JsonOutput.toJson(locator.value()))
		sb.append("}")
		return sb.toString()
	}

	@Override
	int compareTo(TestObjectGist other) {
		if (this.testObjectId.value == other.testObjectId.value) {
			return 0
		} else {
			return this.testObjectId.value.compareTo(other.testObjectId.value)
		}
	}
}
