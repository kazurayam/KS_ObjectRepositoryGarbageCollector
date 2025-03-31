package com.kazurayam.ks.testobject.gc

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.kazurayam.ks.testobject.TestObjectId

public class BackwardReferences {

	Map<TestObjectId, Set<ForwardReference>> backwardReferences;

	BackwardReferences() {
		this.backwardReferences = new TreeMap<>()
	}

	Set<TestObjectId> keySet() {
		return backwardReferences.keySet()
	}

	Iterator<Map.Entry<TestObjectId, Set<ForwardReference>>> iterator() {
		return backwardReferences.entrySet().iterator()
	}

	Set<ForwardReference> get(TestObjectId testObjectId) {
		Objects.requireNonNull(testObjectId)
		return backwardReferences.get(testObjectId)
	}

	void put(TestObjectId testObjectId, ForwardReference forwardReference) {
		Objects.requireNonNull(testObjectId)
		Objects.requireNonNull(forwardReference)
		if (!backwardReferences.containsKey(testObjectId)) {
			Set<ForwardReference> emptySet = new TreeSet<>()
			backwardReferences.put(testObjectId, emptySet)
		}
		Set<ForwardReference> set = backwardReferences.get(testObjectId)
		set.add(forwardReference)
	}

	public int size() {
		return backwardReferences.size()
	}

	@Override
	String toString() {
		return this.toJson()
	}

	String toJson() {
		ObjectMapper mapper = new ObjectMapper()
		SimpleModule module = new SimpleModule("BackwardReferencesSerializer",
				new Version(1, 0, 0, null, null, null))
		module.addSerializer(BackwardReferences.class, new BackwardReferences.BackwardReferencesSerializer())
		module.addSerializer(TestObjectId.class, new TestObjectId.TestObjectIdSerializer())
		module.addSerializer(ForwardReference.class, new ForwardReference.ForwardReferenceSerializer())
		mapper.registerModule(module)
		return mapper.writeValueAsString(this)
	}
	
	static class BackwardReferencesSerializer extends StdSerializer<BackwardReferences> {
		BackwardReferencesSerializer() {
			this(null)
		}
		BackwardReferencesSerializer(Class<BackwardReferences> t) {
			super(t)
		}
		@Override
		void serialize(BackwardReferences br,
				JsonGenerator gen, SerializerProvider serializer) {
			gen.writeStartObject()
			gen.writeFieldName("BackwardReferences")
			gen.writeStartArray()
			br.iterator().each { entry ->
				gen.writeStartObject()
				//
				TestObjectId testObjectId = entry.key
				gen.writeStringField("TestObjectId", testObjectId.value())
				//
				Set<ForwardReference> forwardReferences = entry.value
				gen.writeFieldName("ForwardReferences")
				gen.writeStartArray()
				forwardReferences.each { fr ->
					gen.writeObject(fr)
				}
				gen.writeEndArray()
				//
				gen.writeEndObject()
			}
			gen.writeEndArray()
			gen.writeEndObject()
		}
	}
}
