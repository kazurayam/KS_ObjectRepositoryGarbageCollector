package com.kazurayam.ks.testobject.combine

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.kazurayam.ks.testcase.DigestedLine
import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testobject.TestObjectEssence
import com.kazurayam.ks.testobject.TestObjectId

import java.util.stream.Collectors

public class Database {

	private Set<ForwardReference> db

	Database() {
		db = new TreeSet<>()
	}

	void addAll(Set<ForwardReference> forwardReferences) {
		Objects.requireNonNull(forwardReferences)
		forwardReferences.each { fref ->
			Objects.requireNonNull(fref)
			this.add(fref)
		}
	}

	void add(ForwardReference fref) {
		Objects.requireNonNull(fref)
		db.add(fref)
	}

	int size() {
		return db.size()
	}

	Set<TestObjectId> getAllReferedTestObjectIds() {
	}

	ForwardReference get(int x) {
		return (this.getAll() as List).get(x)
	}

	Set<ForwardReference> getAll() {
		return db
	}


	//-------------------------------------------------------------------------

	Set<ForwardReference> findForwardReferencesFrom(TestCaseId testCaseId) {
		return db.stream()
				.filter({ forwardReference ->
					forwardReference.getTestCaseId() == testCaseId
				})
				.collect(Collectors.toSet())
	}

	boolean containsTestCaseId(TestCaseId testCaseId) {
		return this.findForwardReferencesFrom(testCaseId).size() > 0
	}

	Set<TestCaseId> getAllTestCaseIdsContained() {
		return db.stream()
				.map({ tctoRef ->
					tctoRef.getTestCaseId()
				})
				.collect(Collectors.toSet())
	}

	//-------------------------------------------------------------------------

	Set<ForwardReference> findForwardReferencesTo(TestObjectId testObjectId) {
		return db.stream()
				.filter({ tctoRef ->
					tctoRef.getTestObjectEssence().getTestObjectId() == testObjectId
				})
				.collect(Collectors.toSet())
	}

	boolean containsTestObjectId(TestObjectId testObjectId) {
		return this.findForwardReferencesTo(testObjectId).size() > 0
	}

	Set<TestObjectId> getAllTestObjectIdsContained() {
		return db.stream()
				.map({ tctoRef ->
					tctoRef.getTestObjectEssence().getTestObjectId()
				})
				.collect(Collectors.toSet())
	}

	//-----------------------------------------------------------------

	@Override
	String toString() {
		return toJson()
	}

	String toJson() {
		ObjectMapper mapper = new ObjectMapper()
		SimpleModule module = new SimpleModule("DatabaseSerializer",
				new Version(1, 0, 0, null, null, null))
		module.addSerializer(Database.class, new Database.DatabaseSerializer())
		module.addSerializer(ForwardReference.class, new ForwardReference.ForwardReferenceSerializer())
		module.addSerializer(TestCaseId.class, new TestCaseId.TestCaseIdSerializer())
		module.addSerializer(DigestedLine.class, new DigestedLine.DigestedLineSerializer())
		module.addSerializer(TestObjectEssence.class, new TestObjectEssence.TestObjectEssenceSerializer())
		mapper.registerModule(module)
		return mapper.writeValueAsString(this)
	}

	static class DatabaseSerializer extends StdSerializer<Database> {
		DatabaseSerializer() {
			this(null)
		}
		DatabaseSerializer(Class<Database> t) {
			super(t)
		}
		@Override
		void serialize(Database db,
				JsonGenerator gen, SerializerProvider serializer) {
			gen.writeStartObject()
			gen.writeFieldName("Database")
			gen.writeStartArray()
			db.getAll().each { forwardReference ->
				gen.writeObject(forwardReference)
			}
			gen.writeEndArray()
			gen.writeEndObject()
		}
	}
}
