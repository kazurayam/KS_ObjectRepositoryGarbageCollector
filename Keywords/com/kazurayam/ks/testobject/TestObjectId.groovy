package com.kazurayam.ks.testobject

import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.SelectorMethod

import groovy.json.JsonOutput

public class TestObjectId implements Comparable<TestObjectId>{

	private String value

	TestObjectId(String value) {
		Objects.requireNonNull(value)
		this.value = value
	}

	String value() {
		return value
	}

	TestObjectEssence toTestObjectEssence() {
		TestObject tObj = ObjectRepository.findTestObject(this.value())
		assert tObj != null: "ObjectRepository.findTestObject('${this.value()}') returned null"
		SelectorMethod selectorMethod = tObj.getSelectorMethod()
		Locator locator = new Locator(tObj.getSelectorCollection().getAt(selectorMethod))
		TestObjectEssence essence = new TestObjectEssence(this, selectorMethod.toString(), locator)
		return essence
	}

	@Override
	boolean equals(Object obj) {
		if (!(obj instanceof TestObjectId)) {
			return false
		}
		TestObjectId other = (TestObjectId)obj
		return this.value == other.value
	}

	@Override
	int hashCode() {
		return value.hashCode()
	}

	@Override
	String toString() {
		return value
	}

	@Override
	int compareTo(TestObjectId other) {
		return this.value.compareTo(other.value)
	}

	String toJson() {
		StringBuilder sb = new StringBuilder()
		sb.append("{")
		sb.append(JsonOutput.toJson("TestObjectId"))
		sb.append(":")
		sb.append(JsonOutput.toJson(value))
		sb.append("}")
		sb.toString()
		return sb.toString()
	}
}
