package com.kazurayam.ks.testobject.combine

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testobject.TestObjectId

class BackwardReferencesDatabase {

	Map<TestObjectId, Set<BackwardReferences>> brDatabase;

	BackwardReferencesDatabase() {
		this.brDatabase = new TreeMap<>()
	}

	Set<TestObjectId> keySet() {
		return brDatabase.keySet()
	}

	Iterator<Map.Entry<TestObjectId, Set<BackwardReferences>>> iterator() {
		return brDatabase.entrySet().iterator()
	}

	Set<BackwardReferences> get(TestObjectId testObjectId) {
		Objects.requireNonNull(testObjectId)
		return brDatabase.get(testObjectId)
	}

	void put(TestObjectId testObjectId, BackwardReferences backwardReferences) {
		Objects.requireNonNull(testObjectId)
		Objects.requireNonNull(backwardReferences)
		assert testObjectId == backwardReferences.getTestObjectId() : "${testObjectId} is not equal to ${backwardReferences.getTestObjectId}"
		if (!brDatabase.containsKey(testObjectId)) {
			Set<BackwardReferences> emptySet = new TreeSet<>()
			brDatabase.put(testObjectId, emptySet)
		}
		Set<BackwardReferences> set = brDatabase.get(testObjectId)
		set.add(backwardReferences)
	}

	int size() {
		return brDatabase.size()
	}

	@Override
	String toString() {
		return this.toJson()
	}

	String toJson() {
		ObjectMapper mapper = new ObjectMapper()
		SimpleModule module = new SimpleModule("BackwardReferencesSerializer",
				new Version(1, 0, 0, null, null, null))
		module.addSerializer(BackwardReferencesDatabase.class, new BackwardReferenceDatabaseSerializer())
		module.addSerializer(TestCaseId.class, new TestCaseId.TestCaseIdSerializer())
		module.addSerializer(TestObjectId.class, new TestObjectId.TestObjectIdSerializer())
		module.addSerializer(BackwardReferences.class, new BackwardReferences.BackwardReferencesSerializer())
		module.addSerializer(ForwardReference.class, new ForwardReference.ForwardReferenceSerializer())
		mapper.registerModule(module)
		return mapper.writeValueAsString(this)
	}

	static class BackwardReferenceDatabaseSerializer extends StdSerializer<BackwardReferencesDatabase> {
		BackwardReferenceDatabaseSerializer() {
			this(null)
		}

		BackwardReferenceDatabaseSerializer(Class<BackwardReferencesDatabase> t) {
			super(t)
		}
		@Override
		void serialize(BackwardReferencesDatabase brm,
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
				Set<BackwardReferences> backwardReferences = entry.value
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
