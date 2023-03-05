package com.kazurayam.ks.testobject


public class Locator implements Comparable<Locator> {

	private String value

	Locator(String value) {
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
}
