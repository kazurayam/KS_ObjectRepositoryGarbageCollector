package com.kazurayam.ks.gc

import java.util.stream.Collectors

import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testobject.TestObjectId
import groovy.json.JsonOutput

public class Database {

	private Set<TCTOReference> db

	Database() {
		db = new TreeSet<>()
	}

	void addAll(Set<TCTOReference> references) {
		Objects.requireNonNull(references)
		references.forEach { ref ->
			this.add(ref)
		}
	}

	void add(TCTOReference reference) {
		Objects.requireNonNull(reference)
		db.add(reference)
	}

	int size() {
		return db.size()
	}

	TCTOReference get(int x) {
		return (this.getAll() as List).get(x)
	}

	Set<TCTOReference> getAll() {
		return new TreeSet<>(db)
	}

	@Override
	String toString() {
		return toJson()
	}

	String toJson() {
		StringBuilder sb = new StringBuilder()
		sb.append("{")
		sb.append(JsonOutput.toJson("Database"))
		sb.append(":")
		sb.append('[')
		String sep1 = ""
		List list = db as List
		list.forEach { TCTOReference ref ->
			sb.append(sep1)
			sb.append(ref.toJson())
			sep1 = ','
		}
		sb.append(']')
		sb.append("}")
		return JsonOutput.prettyPrint(sb.toString())
	}

	//-------------------------------------------------------------------------

	Set<TCTOReference> findTCTOReferencesOf(TestCaseId testCaseId) {
		return db.stream()
				.filter({ tctoRef ->
					tctoRef.testCaseId() == testCaseId
				})
				.collect(Collectors.toSet())
	}

	boolean containsTestCaseId(TestCaseId testCaseId) {
		return this.findTCTOReferencesOf(testCaseId).size() > 0
	}

	Set<TestCaseId> getAllTestCaseIdsContained() {
		return db.stream()
				.map({ tctoRef ->
					tctoRef.testCaseId()
				})
				.collect(Collectors.toSet())
	}

	//-------------------------------------------------------------------------

	Set<TCTOReference> findTCTOReferencesOf(TestObjectId testObjectId) {
		return db.stream()
				.filter({ tctoRef ->
					tctoRef.testObjectGist().testObjectId() == testObjectId
				})
				.collect(Collectors.toSet())
	}

	boolean containsTestObjectId(TestObjectId testObjectId) {
		return this.findTCTOReferencesOf(testObjectId).size() > 0
	}

	Set<TestObjectId> getAllTestObjectIdsContained() {
		return db.stream()
				.map({ tctoRef ->
					tctoRef.testObjectGist().testObjectId()
				})
				.collect(Collectors.toSet())
	}

}
