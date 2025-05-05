package com.kazurayam.ks.testobject

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Given with a Path "Object Repository\\main\\Page_CURA Healthcare Service\\a_Make Appointment.rs",
 * this.getValue() will return a String "main/Page_CURA Healthcare Service/a_Make Appointment"
 */
class TestObjectId implements Comparable<TestObjectId>{

	private String value

	TestObjectId(Path relativePath) {
		Objects.requireNonNull(relativePath)
		if (relativePath.isAbsolute()) {
			throw new IllegalArgumentException("argument ${relativePath} is not relative")
		}
		String filePath = relativePath.normalize().toString()
				.replaceAll('\\.rs$', '')
		this.value = filePath.replace("\\", "/")
	}

	TestObjectId(TestObjectId that) {
		this(that.getRelativePath())
	}

	String getValue() {
		return value
	}

	Path getRelativePath() {
		return Paths.get(value + ".rs")
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
		ObjectMapper mapper = new ObjectMapper()
		SimpleModule module = new SimpleModule("TestObjectIdSerializer",
				new Version(1, 0, 0, null, null, null))
		module.addSerializer(TestObjectId.class, new TestObjectIdSerializer())
		mapper.registerModules(module)
		return mapper.writeValueAsString(this)
	}

	/*
	 * 
	 */
	static class TestObjectIdSerializer extends StdSerializer<TestObjectId> {
		TestObjectIdSerializer() {
			this(null)
		}
		TestObjectIdSerializer(Class<TestObjectId> t) {
			super(t)
		}
		@Override
		void serialize(TestObjectId testObjectId,
				JsonGenerator gen, SerializerProvider serializer) {
			gen.writeString(testObjectId.getValue())
		}
	}
}
