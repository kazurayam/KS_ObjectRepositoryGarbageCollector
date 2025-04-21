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

	public static Locator NULL_OBJECT = new Locator("")

	private String value = ""

	Locator(String value) {
		if (value != null) {
			this.value = value
		} else {
			this.value = ""
		}
	}

	String getValue() {
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
		LocatorSerializer(Class<LocatorSerializer> t) {
			super(t)
		}
		@Override
		void serialize(Locator locator,
				JsonGenerator gen, SerializerProvider serializer) {
			gen.writeStartObject()
			gen.writeStringField("Locator", locator.getValue())
			gen.writeEndObject()
		}
	}
}
