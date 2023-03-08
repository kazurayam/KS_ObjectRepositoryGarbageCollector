package com.kazurayam.ks.testobject

import groovy.json.JsonOutput

public class TestObjectGist implements Comparable<TestObjectGist> {

	private TestObjectId id       // "Page_CURA Healthcare Service/a_Go to Homepage"
	private String method   // "BASIC", "XPATH", "CSS"
	private Locator locator  // "//section[@id='summary']/div/div/div[7]/p/a"

	TestObjectGist(TestObjectId id, String method, Locator locator) {
		Objects.requireNonNull(id)
		Objects.requireNonNull(method)
		Objects.requireNonNull(locator)
		this.id = id
		this.method = method
		this.locator = locator
	}

	TestObjectId id() {
		return id
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
		return this.id == other.id &&
				this.method == other.method &&
				this.locator == other.locator
	}

	@Override
	int hashCode() {
		int hash = 7;
		hash = 31 * hash + id.hashCode();
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
		sb.append(JsonOutput.toJson(id.value()))
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
		if (this.id.value == other.id.value) {
			return 0
		} else {
			return this.id.value.compareTo(other.id.value)
		}
	}
}
