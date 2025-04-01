package com.kazurayam.ks.testobject.gc

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer

import com.kazurayam.ks.testobject.TestObjectId

/**
 * Set of unused TestObjectIds
 */
class Garbages {

	private Set<TestObjectId> garbages

	Garbages() {
		garbages = new TreeSet<>()
	}

	void add(TestObjectId garbage) {
		Objects.requireNonNull(garbage)
		garbages.add(garbage)
	}

	int size() {
		return garbages.size()
	}

	TestObjectId get(int x) {
		return (this.getAllTestObjectIds() as List).get(x)
	}

	Set<TestObjectId> getAllTestObjectIds() {
		return new TreeSet<>(garbages)
	}

	//-----------------------------------------------------------------

	@Override
	String toString() {
		return toJson()
	}

	String toJson() {
		ObjectMapper mapper = new ObjectMapper()
		SimpleModule module = new SimpleModule("GarbagesSerializer",
				new Version(1, 0, 0, null, null, null))
		module.addSerializer(TestObjectId.class, new TestObjectId.TestObjectIdSerializer())
		module.addSerializer(Garbages.class, new Garbages.GarbagesSerializer())
		mapper.registerModule(module)
		return mapper.writeValueAsString(this)
	}

	static class GarbagesSerializer extends StdSerializer<Garbages> {
		GarbagesSerializer() {
			this(null)
		}
		GarbagesSerializer(Class<Garbages> t) {
			super(t)
		}
		@Override
		void serialize(Garbages garbages,
				JsonGenerator gen, SerializerProvider serializer) {
			gen.writeStartObject()
			gen.writeFieldName("Garbages")
			gen.writeStartArray()
			garbages.getAllTestObjectIds().each { testObjectId ->
				gen.writeObject(testObjectId)
			}
			gen.writeEndArray()
			gen.writeEndObject()
		}
	}
}
