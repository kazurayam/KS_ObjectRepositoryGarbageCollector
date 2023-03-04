package com.kazurayam.ks.testcase

import groovy.json.JsonOutput

public class ExternalReferences {

	private Map<TestCaseId, List<TextSearchResult>> collection

	ExternalReferences() {
		collection = new TreeMap<>()
	}

	void put(TestCaseId key, List<TextSearchResult> tsrList) {
		Objects.requireNonNull(key)
		Objects.requireNonNull(tsrList)
		tsrList.forEach { tsr ->
			this.put(key, tsr)
		}
	}

	void put(TestCaseId key, TextSearchResult tsr) {
		Objects.requireNonNull(key)
		Objects.requireNonNull(tsr)
		List<TextSearchResult> tsrList
		if (collection.containsKey(key)) {
			tsrList = collection.get(key)
		} else {
			tsrList = new ArrayList<>()
		}
		tsrList.add(tsr)
		collection.put(key, tsrList)
	}

	Set<TestCaseId> keySet() {
		return collection.keySet()
	}

	boolean containsKey(TestCaseId key) {
		return collection.containsKey(key)
	}

	List<TextSearchResult> get(TestCaseId key) {
		return collection.get(key)
	}

	int size() {
		return collection.size()
	}

	@Override
	String toString() {
		return toJson()
	}

	String toJson() {
		StringBuilder sb = new StringBuilder()
		sb.append('{')
		String sep1 = ""
		collection.keySet().forEach { TestCaseId k ->
			sb.append(sep1)
			sb.append(JsonOutput.toJson(k))
			sb.append(':')
			List<TextSearchResult> tsrList = collection.get(k)
			sb.append('[')
			String sep2 = ""
			tsrList.forEach { tsr ->
				sb.append(sep2)
				sb.append(tsr.toJson())
				sep2 = ","
			}
			sb.append(']')
			sep1 = ','
		}
		sb.append('}')
		return sb.toString()
	}
}
