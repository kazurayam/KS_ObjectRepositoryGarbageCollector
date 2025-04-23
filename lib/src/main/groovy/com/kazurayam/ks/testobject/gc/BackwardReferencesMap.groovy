package com.kazurayam.ks.testobject.gc

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testobject.TestObjectEssence
import com.kazurayam.ks.testobject.TestObjectId

class BackwardReferencesMap {

	Map<TestObjectId, Set<ForwardReference>> backwardReferenceMap;

	BackwardReferencesMap() {
		this.backwardReferenceMap = new TreeMap<>()
	}

	Set<TestObjectId> keySet() {
		return backwardReferenceMap.keySet()
	}

	Iterator<Map.Entry<TestObjectId, Set<ForwardReference>>> iterator() {
		return backwardReferenceMap.entrySet().iterator()
	}

	Set<ForwardReference> get(TestObjectId testObjectId) {
		Objects.requireNonNull(testObjectId)
		return backwardReferenceMap.get(testObjectId)
	}

	void put(TestObjectId testObjectId, ForwardReference forwardReference) {
		Objects.requireNonNull(testObjectId)
		Objects.requireNonNull(forwardReference)
		if (!backwardReferenceMap.containsKey(testObjectId)) {
			Set<ForwardReference> emptySet = new TreeSet<>()
			backwardReferenceMap.put(testObjectId, emptySet)
		}
		Set<ForwardReference> set = backwardReferenceMap.get(testObjectId)
		set.add(forwardReference)
	}

	int size() {
		return backwardReferenceMap.size()
	}

	@Override
	String toString() {
		return this.toJson()
	}

	String toJson() {
		ObjectMapper mapper = new ObjectMapper()
		SimpleModule module = new SimpleModule("BackwardReferencesSerializer",
				new Version(1, 0, 0, null, null, null))
		module.addSerializer(BackwardReferencesMap.class, new BackwardReferenceMapSerializer())
		module.addSerializer(TestCaseId.class, new TestCaseId.TestCaseIdSerializer())
		module.addSerializer(TestObjectEssence.class, new TestObjectEssence.TestObjectEssenceSerializer())
		module.addSerializer(TestObjectId.class, new TestObjectId.TestObjectIdSerializer())
		module.addSerializer(ForwardReference.class, new ForwardReference.ForwardReferenceSerializer())
		mapper.registerModule(module)
		return mapper.writeValueAsString(this)
	}

	static class BackwardReferenceMapSerializer extends StdSerializer<BackwardReferencesMap> {
		BackwardReferenceMapSerializer() {
			this(null)
		}

		BackwardReferenceMapSerializer(Class<BackwardReferencesMap> t) {
			super(t)
		}
		@Override
		void serialize(BackwardReferencesMap brm,
					   JsonGenerator gen, SerializerProvider serializer) {
			gen.writeStartObject()
			gen.writeFieldName("BackwardReferenceMap")
			gen.writeStartArray()
			brm.iterator().each { entry ->
				gen.writeStartObject()
				//
				TestObjectId testObjectId = entry.key
				gen.writeStringField("TestObjectId", testObjectId.getValue())
				//
				Set<ForwardReference> forwardReferences = entry.value
				gen.writeNumberField("Number of references", forwardReferences.size())
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
