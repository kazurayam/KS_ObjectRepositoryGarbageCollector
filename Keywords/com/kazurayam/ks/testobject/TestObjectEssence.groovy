package com.kazurayam.ks.testobject

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer

import groovy.json.JsonOutput

public class TestObjectEssence implements Comparable<TestObjectEssence> {

	private TestObjectId testObjectId       // "Page_CURA Healthcare Service/a_Go to Homepage"
	private String method   // "BASIC", "XPATH", "CSS"
	private Locator locator  // "//section[@id='summary']/div/div/div[7]/p/a" ; could be null

	TestObjectEssence(TestObjectId testObjectId, String method, Locator locator) {
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

	public valueAsJsonByJackson() {
		ObjectMapper mapper = new ObjectMapper()
		SimpleModule module = new SimpleModule("TestObjectEssenceSerializer",
				new Version(1,0.0, null, null, null))
		module.addSerializer(TestObjectEssence.class, new TestObjectEssenceSerializer())
		mapper.registerModule(module)
		return mapper.writeValueAsString(this)
	}

	@Override
	int compareTo(TestObjectEssence other) {
		if (this.testObjectId.value == other.testObjectId.value) {
			return 0
		} else {
			return this.testObjectId.value.compareTo(other.testObjectId.value)
		}
	}

	static class TestObjectEssenceSerializer extends StdSerializer<TestObjectEssence> {
		TestObjectEssenceSerializer() {
			this(null)
		}
		TestObjectEssenceSerializer(Class<TestObjectEssence> t) {
			super(t)
		}
		@Override
		void serialize(TestObjectEssence essence,
				JsonGenerator jsonGenerator, SerializerProvider serializer) {
			jsonGenerator.writeStartObject()
			jsonGenerator.writeStringField("testObjectId", essence.testObjectId().toString())
			jsonGenerator.writeEndObject()
		}
	}
}
