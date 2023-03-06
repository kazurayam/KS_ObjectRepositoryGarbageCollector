package com.kazurayam.ks.testcase

import java.nio.file.Path
import groovy.json.JsonOutput

public class TestCaseId implements Comparable<TestCaseId> {

	private String value

	TestCaseId(String value) {
		Objects.requireNonNull(value)
		assert !(value.startsWith('/')), "value='${value}' should not start with '/'"
		this.value = value
	}

	TestCaseId(Path scriptsDir, Path groovyFile) {
		Path relative = scriptsDir.relativize(groovyFile)
		this.value = relative.getParent().toString()
	}

	String value() {
		return value
	}

	@Override
	boolean equals(Object obj) {
		if (!(obj instanceof TestCaseId)) {
			return false
		}
		TestCaseId other = (TestCaseId)obj
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

	String toJson() {
		return JsonOutput.toJson(value)
	}

	@Override
	int compareTo(TestCaseId other) {
		return this.value.compareTo(other.value)
	}
}
