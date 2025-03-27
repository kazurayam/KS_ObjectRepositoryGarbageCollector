package com.kazurayam.ks.testobject

import groovy.json.JsonOutput

public class TestObjectEssence implements Comparable<TestObjectEssence> {

	private TestObjectId testObjectId       // "Page_CURA Healthcare Service/a_Go to Homepage"
	private String method   // "BASIC", "XPATH", "CSS"
	private Locator locator  // "//section[@id='summary']/div/div/div[7]/p/a" ; could be null

	TestObjectEssence(TestObjectId testObjectId, String method, Locator locator) {
		Objects.requireNonNull(testObjectId)
		this.testObjectId = testObjectId
		if (method != null) {
			this.method = method
		}
		if (locator != null) {
			this.locator = locator
		}
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
		if (!(obj instanceof TestObjectEssence)) {
			return false
		}
		TestObjectEssence other = (TestObjectEssence)obj
		return this.testObjectId == other.testObjectId;
	}

	@Override
	int hashCode() {
		int hash = 7;
		hash = 31 * hash + testObjectId.hashCode();
		if (method != null) {
			hash = 31 * hash + method.hashCode()
		}
		if (locator != null) {
			hash = 31 * hash + locator.hashCode()
		}
		return hash;
	}

	@Override
	String toString() {
		return this.toJson()
	}

	String toJson() {
		StringBuilder sb = new StringBuilder()
		sb.append("{")
		sb.append(JsonOutput.toJson("TestObjectEssence"))
		sb.append(":")
		sb.append(valueAsJson())
		sb.append("}")
		return JsonOutput.prettyPrint(sb.toString())
	}

	public valueAsJson() {
		StringBuilder sb = new StringBuilder()
		sb.append("{")
		sb.append(JsonOutput.toJson("TestObjectId"))
		sb.append(":")
		sb.append(JsonOutput.toJson(testObjectId.value()))
		sb.append(",")
		sb.append(JsonOutput.toJson("Method"))
		sb.append(":")
		sb.append(JsonOutput.toJson(method))
		sb.append(",")
		sb.append(JsonOutput.toJson("Locator"))
		sb.append(":")
		sb.append(JsonOutput.toJson(locator.value()))
		sb.append("}")
		return JsonOutput.prettyPrint(sb.toString())
	}

	@Override
	int compareTo(TestObjectEssence other) {
		if (this.testObjectId.value == other.testObjectId.value) {
			return 0
		} else {
			return this.testObjectId.value.compareTo(other.testObjectId.value)
		}
	}
}
