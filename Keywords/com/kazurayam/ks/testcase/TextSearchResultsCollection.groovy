package com.kazurayam.ks.testcase

import groovy.json.JsonOutput

public class TextSearchResultsCollection {

	private Map<String, List<TextSearchResult>> collection

	TextSearchResultsCollection() {
		collection = new TreeMap<>()
	}

	void put(String key, TextSearchResult tsr) {
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

	Set<String> keySet() {
		return collection.keySet()
	}

	boolean containsKey(String key) {
		return collection.containsKey(key)
	}

	List<TextSearchResult> get(String key) {
		return collection.get(key)
	}

	@Override
	String toString() {
		return toJson()
	}

	String toJson() {
		StringBuilder sb = new StringBuilder()
		sb.append('{')
		String sep1 = ""
		collection.keySet().forEach { String k ->
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
