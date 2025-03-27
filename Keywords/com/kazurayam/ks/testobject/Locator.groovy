package com.kazurayam.ks.testobject

import groovy.json.JsonOutput

public class Locator implements Comparable<Locator> {

	private String value

	Locator(String value) {
		Objects.requireNonNull(value)
		this.value = value
	}

	String value() {
		return value
	}

	@Override
	boolean equals(Object obj) {
		if (!(obj instanceof Locator)) {
			return false
		}
		Locator other = (Locator)obj
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
	int compareTo(Locator other) {
		return this.value.compareTo(other.value)
	}

	String toJson() {
		StringBuilder sb = new StringBuilder()
		sb.append("{")
		sb.append(JsonOutput.toJson("Locator"))
		sb.append(":")
		sb.append(JsonOutput.toJson(value))
		sb.append("}")
		return sb.toString()
	}
}
