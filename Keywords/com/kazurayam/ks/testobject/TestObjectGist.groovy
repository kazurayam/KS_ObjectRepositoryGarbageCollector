package com.kazurayam.ks.testobject

import groovy.json.JsonOutput

public class TestObjectGist {
	
	private String id       // "Page_CURA Healthcare Service/a_Go to Homepage"
	private String method   // "BASIC", "XPATH", "CSS"
	private String locator  // "//section[@id='summary']/div/div/div[7]/p/a"
	
	TestObjectGist(String id, String method, String locator) {
		Objects.requireNonNull(id)
		Objects.requireNonNull(method)
		Objects.requireNonNull(locator)
		this.id = id
		this.method = method
		this.locator = locator
	}
	
	String id() {
		return id
	}
	
	String method() {
		return method
	}
	
	String locator() {
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
		hash = 31 * hash + id;
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
		sb.append("\"id\":")
		sb.append(JsonOutput.toJson(id))
		sb.append(",")
		sb.append("\"method\":")
		sb.append(JsonOutput.toJson(method))
		sb.append(",")
		sb.append("\"locator\":")
		sb.append(JsonOutput.toJson(locator))
		sb.append("}")
		return sb.toString()
	}
}
