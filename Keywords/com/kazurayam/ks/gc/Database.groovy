package com.kazurayam.ks.gc

import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testcase.TextSearchResult
import com.kazurayam.ks.gc.TCTOReference

import groovy.json.JsonOutput

public class Database {

	private Map<TestCaseId, Set<TCTOReference>> db

	Database() {
		db = new TreeMap<>()
	}

	void put(TestCaseId key, Set<TCTOReference> references) {
		Objects.requireNonNull(key)
		Objects.requireNonNull(references)
		references.forEach { ref ->
			this.put(key, ref)
		}
	}

	void put(TestCaseId key, TCTOReference reference) {
		Objects.requireNonNull(key)
		Objects.requireNonNull(reference)
		Set<TCTOReference> refs
		if (db.containsKey(key)) {
			refs = db.get(key)
		} else {
			refs = new TreeSet<>()
		}
		refs.add(reference)
		db.put(key, refs)
	}

	Set<TestCaseId> keySet() {
		return db.keySet()
	}

	boolean containsKey(TestCaseId key) {
		return db.containsKey(key)
	}

	Set <TCTOReference> get(TestCaseId key) {
		return db.get(key)
	}

	int size() {
		return db.size()
	}

	@Override
	String toString() {
		return toJson()
	}

	String toJson() {
		StringBuilder sb = new StringBuilder()
		sb.append('{')
		String sep1 = ""
		db.keySet().forEach { TestCaseId k ->
			sb.append(sep1)
			sb.append(JsonOutput.toJson(k))
			sb.append(':')
			Set<TCTOReference> setRef = db.get(k)
			sb.append('[')
			String sep2 = ""
			setRef.forEach { ref ->
				sb.append(sep2)
				sb.append(ref.toJson())
				sep2 = ","
			}
			sb.append(']')
			sep1 = ','
		}
		sb.append('}')
		return sb.toString()
	}
}
