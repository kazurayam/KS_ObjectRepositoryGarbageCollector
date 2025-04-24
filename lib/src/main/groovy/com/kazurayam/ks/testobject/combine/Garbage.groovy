package com.kazurayam.ks.testobject.combine

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.kazurayam.ks.testobject.TestObjectId

/**
 * Set of unused TestObjectIds
 */
class Garbage {

	private Set<TestObjectId> garbage

	Garbage() {
		garbage = new TreeSet<>()
	}

	void add(TestObjectId garbage) {
		Objects.requireNonNull(garbage)
		this.garbage.add(garbage)
	}

	int size() {
		return garbage.size()
	}

	Iterator<TestObjectId> iterator() {
		return garbage.iterator()
	}

	TestObjectId get(int x) {
		return (this.getAllTestObjectIds() as List).get(x)
	}

	Set<TestObjectId> getAllTestObjectIds() {
		return new TreeSet<>(garbage)
	}

	//-----------------------------------------------------------------

	@Override
	String toString() {
		return toJson()
	}

	String toJson() {
		ObjectMapper mapper = new ObjectMapper()
		SimpleModule module = new SimpleModule("GarbageSerializer",
				new Version(1, 0, 0, null, null, null))
		module.addSerializer(TestObjectId.class, new TestObjectId.TestObjectIdSerializer())
		module.addSerializer(Garbage.class, new GarbageSerializer())
		mapper.registerModule(module)
		return mapper.writeValueAsString(this)
	}

	static class GarbageSerializer extends StdSerializer<Garbage> {
		GarbageSerializer() {
			this(null)
		}

		GarbageSerializer(Class<Garbage> t) {
			super(t)
		}
		@Override
		void serialize(Garbage garbage,
					   JsonGenerator gen, SerializerProvider serializer) {
			gen.writeStartObject()
			gen.writeFieldName("Garbage")
			gen.writeStartArray()
			garbage.getAllTestObjectIds().each { testObjectId ->
				gen.writeObject(testObjectId)
			}
			gen.writeEndArray()
			gen.writeEndObject()
		}
	}
}
