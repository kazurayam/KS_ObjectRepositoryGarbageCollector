package com.kazurayam.ks.testobject.combine

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testobject.TestObjectId

class BackwardReferenceIndex {

	Map<TestObjectId, Set<BackwardReference>> index;

	BackwardReferenceIndex() {
		this.index = new TreeMap<>()
	}

	Set<TestObjectId> keySet() {
		return index.keySet()
	}

	Iterator<Map.Entry<TestObjectId, Set<BackwardReference>>> iterator() {
		return index.entrySet().iterator()
	}

	Set<BackwardReference> get(TestObjectId testObjectId) {
		Objects.requireNonNull(testObjectId)
		return index.get(testObjectId)
	}

	void put(TestObjectId testObjectId, BackwardReference backwardReferences) {
		Objects.requireNonNull(testObjectId)
		Objects.requireNonNull(backwardReferences)
		assert testObjectId == backwardReferences.getTestObjectId() : "${testObjectId} is not equal to ${backwardReferences.getTestObjectId}"
		if (!index.containsKey(testObjectId)) {
			Set<BackwardReference> emptySet = new TreeSet<>()
			index.put(testObjectId, emptySet)
		}
		Set<BackwardReference> set = index.get(testObjectId)
		set.add(backwardReferences)
	}

	int size() {
		return index.size()
	}

	@Override
	String toString() {
		return this.toJson()
	}

	String toJson() {
		ObjectMapper mapper = new ObjectMapper()
		SimpleModule module = new SimpleModule("BackwardReferencesDatabaseSerializer",
				new Version(1, 0, 0, null, null, null))
		module.addSerializer(BackwardReferenceIndex.class, new BackwardReferenceDatabaseSerializer())
		module.addSerializer(TestCaseId.class, new TestCaseId.TestCaseIdSerializer())
		module.addSerializer(TestObjectId.class, new TestObjectId.TestObjectIdSerializer())
		module.addSerializer(BackwardReference.class, new BackwardReference.BackwardReferencesSerializer())
		module.addSerializer(ForwardReference.class, new ForwardReference.ForwardReferenceSerializer())
		mapper.registerModule(module)
		return mapper.writeValueAsString(this)
	}

	static class BackwardReferenceDatabaseSerializer extends StdSerializer<BackwardReferenceIndex> {
		BackwardReferenceDatabaseSerializer() {
			this(null)
		}

		BackwardReferenceDatabaseSerializer(Class<BackwardReferenceIndex> t) {
			super(t)
		}
		@Override
		void serialize(BackwardReferenceIndex brm,
					   JsonGenerator gen, SerializerProvider serializer) {
			gen.writeStartObject()
			gen.writeFieldName("BackwardReferenceIndex")
			gen.writeStartArray()
			brm.iterator().each { entry ->
				gen.writeStartObject()
				//
				TestObjectId testObjectId = entry.key
				gen.writeStringField("TestObjectId", testObjectId.getValue())
				//
				Set<BackwardReference> backwardReferences = entry.value
				gen.writeNumberField("Number of TestObjects", backwardReferences.size())
				gen.writeFieldName("TestObjects")
				gen.writeStartArray()
				backwardReferences.each { br ->
					gen.writeObject(br)
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
