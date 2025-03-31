package com.kazurayam.ks.testobject.gc

import java.util.stream.Collectors

import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testobject.TestObjectId
import groovy.json.JsonOutput

public class Database {

	private Set<ForwardReference> db

	Database() {
		db = new TreeSet<>()
	}

	void addAll(Set<ForwardReference> references) {
		Objects.requireNonNull(references)
		references.each { ref ->
			this.add(ref)
		}
	}

	void add(ForwardReference reference) {
		Objects.requireNonNull(reference)
		db.add(reference)
	}

	int size() {
		return db.size()
	}

	ForwardReference get(int x) {
		return (this.getAll() as List).get(x)
	}

	Set<ForwardReference> getAll() {
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
		list.forEach { ForwardReference ref ->
			sb.append(sep1)
			sb.append(ref.toJson())
			sep1 = ','
		}
		sb.append(']')
		sb.append("}")
		return JsonOutput.prettyPrint(sb.toString())
	}

	//-------------------------------------------------------------------------

	Set<ForwardReference> findForwardReferencesFrom(TestCaseId testCaseId) {
		return db.stream()
				.filter({ forwardReference ->
					forwardReference.testCaseId() == testCaseId
				})
				.collect(Collectors.toSet())
	}

	boolean containsTestCaseId(TestCaseId testCaseId) {
		return this.findForwardReferencesFrom(testCaseId).size() > 0
	}

	Set<TestCaseId> getAllTestCaseIdsContained() {
		return db.stream()
				.map({ tctoRef ->
					tctoRef.testCaseId()
				})
				.collect(Collectors.toSet())
	}

	//-------------------------------------------------------------------------

	Set<ForwardReference> findForwadReferenceTo(TestObjectId testObjectId) {
		return db.stream()
				.filter({ tctoRef ->
					tctoRef.testObjectEssence().testObjectId() == testObjectId
				})
				.collect(Collectors.toSet())
	}

	boolean containsTestObjectId(TestObjectId testObjectId) {
		return this.findForwadReferenceTo(testObjectId).size() > 0
	}

	Set<TestObjectId> getAllTestObjectIdsContained() {
		return db.stream()
				.map({ tctoRef ->
					tctoRef.testObjectEssence().testObjectId()
				})
				.collect(Collectors.toSet())
	}
}
