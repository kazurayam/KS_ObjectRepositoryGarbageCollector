package com.kazurayam.ks.testcase

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer

import java.nio.file.Path

public class TestCaseId implements Comparable<TestCaseId> {

	private String value

	TestCaseId(String value) {
		Objects.requireNonNull(value)
		assert !(value.startsWith('/')), "value='${value}' should not start with '/'"
		this.value = value
	}

	static TestCaseId resolveTestCaseId(Path scriptsDir, Path groovyFile) {
		Path relative = scriptsDir.relativize(groovyFile).normalize()
		TestCaseId testCaseId = new TestCaseId(relative.getParent().toString())
		return testCaseId
	}

	String getValue() {
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
	int compareTo(TestCaseId other) {
		return this.value.compareTo(other.value)
	}

	@Override
	String toString() {
		return value
	}

	public toJson() {
		ObjectMapper mapper = new ObjectMapper()
		SimpleModule module = new SimpleModule("TestCaseIdSerializer",
				new Version(1, 0, 0, null, null, null))
		module.addSerializer(TestCaseId.class, new TestCaseId.TestCaseIdSerializer())
		mapper.registerModule(module)
		return mapper.writeValueAsString(this)
	}

	static class TestCaseIdSerializer extends StdSerializer<TestCaseId> {
		TestCaseIdSerializer() {
			this(null)
		}
		TestCaseIdSerializer(Class<TestCaseIdSerializer> t) {
			super(t)
		}
		@Override
		void serialize(TestCaseId testCaseId,
				JsonGenerator gen, SerializerProvider serializer) {
			gen.writeString(testCaseId.getValue())
		}
	}
}
