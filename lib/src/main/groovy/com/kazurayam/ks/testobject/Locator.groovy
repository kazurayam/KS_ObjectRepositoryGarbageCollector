package com.kazurayam.ks.testobject

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer

/**
 * Locator is a part of Test Object; XPath expression, CSS Selector, etc
 */
class Locator implements Comparable<Locator> {

	public static Locator NULL_OBJECT = new Locator("", SelectorMethod.BASIC)

	private String value = ""
	private SelectorMethod method

	Locator(String value, SelectorMethod method) {
		Objects.requireNonNull(value)
		Objects.requireNonNull(method)
		this.value = value
		this.method = method
	}

	String getValue() {
		return value
	}

	SelectorMethod getSelectorMethod() {
		return method
	}

	@Override
	boolean equals(Object obj) {
		if (!(obj instanceof Locator)) {
			return false
		}
		Locator other = (Locator)obj
		return this.value == other.value
				&& this.method == other.method
	}

	@Override
	int hashCode() {
		return value.hashCode()
	}

	@Override
	String toString() {
		return toJson()
	}

	@Override
	int compareTo(Locator other) {
		int v = this.value <=> other.value
		if (v != 0) {
			return v
		} else {
			return this.selectorMethod <=> other.selectorMethod
		}
	}

	String toJson() {
		ObjectMapper mapper = new ObjectMapper()
		SimpleModule module = new SimpleModule("LocatorSerializer",
				new Version(1, 0, 0, null, null, null))
		module.addSerializer(Locator.class, new LocatorSerializer())
		mapper.registerModules(module)
		return mapper.writeValueAsString(this)
	}

	static class LocatorSerializer extends StdSerializer<Locator> {
		LocatorSerializer() {
			this(null)
		}
		LocatorSerializer(Class<Locator> t) {
			super(t)
		}
		@Override
		void serialize(Locator locator,
				JsonGenerator gen, SerializerProvider serializer) {
			gen.writeStartObject()
			gen.writeStringField("Locator", locator.getValue())
			gen.writeStringField("Method", locator.getSelectorMethod().toString())
			gen.writeEndObject()
		}
	}
}
