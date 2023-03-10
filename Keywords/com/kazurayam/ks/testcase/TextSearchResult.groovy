package com.kazurayam.ks.testcase

import groovy.json.JsonOutput

public class TextSearchResult implements Comparable<TextSearchResult> {

	private String line
	private int lineNo
	//
	private String pattern
	private Boolean isRegex
	//
	private int matchAt
	private int matchEnd
	private Boolean hasMatch

	private TextSearchResult(Builder builder) {
		this.line = builder.line
		this.lineNo = builder.lineNo
		this.pattern = builder.pattern
		this.isRegex = builder.isRegex
		this.matchAt = builder.matchAt
		this.matchEnd = builder.matchEnd
		this.hasMatch = builder.hasMatch
	}

	public String line() {
		return this.line
	}

	public int lineNo() {
		return this.lineNo
	}

	public String pattern() {
		return this.pattern
	}

	public Boolean isRegex() {
		return this.isRegex
	}

	public int matchAt() {
		return this.matchAt
	}

	public int matchEnd() {
		return this.matchEnd
	}

	public Boolean hasMatch() {
		return this.hasMatch
	}

	@Override
	public String toString() {
		this.toJson()
	}

	public String toJson() {
		StringBuilder sb = new StringBuilder()
		sb.append("{")
		sb.append(JsonOutput.toJson("TextSearchResult"))
		sb.append(":")
		sb.append("{")
		sb.append('"lineNo":')
		sb.append(JsonOutput.toJson(lineNo))
		sb.append(',')
		sb.append('"line":')
		sb.append(JsonOutput.toJson(line))
		sb.append(',')
		sb.append('"pattern":')
		sb.append(JsonOutput.toJson(pattern))
		sb.append(',')
		sb.append('"isRegex":')
		sb.append(JsonOutput.toJson(isRegex))
		sb.append(',')
		sb.append('"matchAt":')
		sb.append(JsonOutput.toJson(matchAt))
		sb.append(',')
		sb.append('"matchEnd":')
		sb.append(JsonOutput.toJson(matchEnd))
		sb.append(',')
		sb.append('"hasMatch":')
		sb.append(JsonOutput.toJson(hasMatch))
		sb.append("}")
		sb.append("}")
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TextSearchResult))	{
			return false
		}
		TextSearchResult other = (TextSearchResult)obj
		return this.lineNo == other.lineNo &&
				this.line == other.line &&
				this.pattern == other.pattern &&
				this.isRegex == other.isRegex
		// ignore matchAt, matchEnd, hasMatch properties. necessary?
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + lineNo;
		hash = 31 * hash + line.hashCode()
		hash = 31 * hash + pattern.hashCode()
		return hash;
	}

	@Override
	int compareTo(TextSearchResult other) {
		int v
		v = this.lineNo - other.lineNo
		if (v != 0) {
			return v
		} else {
			v = this.line.compareTo(other.line)
			if (v != 0) {
				return v
			} else {
				v = this.pattern.compareTo(other.pattern)
				if (v != 0) {
					return v
				} else {
					Boolean b1 = (Boolean)this.isRegex
					Boolean b2 = (Boolean)other.isRegex
					return b1.compareTo(b2)
				}
			}
		}
	}

	/**
	 * 
	 * 
	 */
	public static class Builder {
		String line
		int lineNo
		//
		String pattern
		Boolean isRegex
		//
		int matchAt
		int matchEnd
		Boolean hasMatch

		/**
		 * @param line
		 * @param lineNo 1,2,3,4,...
		 */
		Builder(String line, int lineNo) {
			this.line = line
			this.lineNo = lineNo
			this.pattern = ""
			this.isRegex = false
			this.matchAt = 0
			this.matchEnd = 0
			this.hasMatch = false
		}

		Builder pattern(String pattern, Boolean isRegex) {
			Objects.requireNonNull(pattern)
			this.pattern = pattern
			this.isRegex = isRegex
			return this
		}

		/**
		 * @param matchAt 1,2,3,4,...
		 * @param matchEnd 1,2,3,4,...
		 */
		Builder matchFound(int matchAt, int matchEnd) {
			assert matchAt > 0
			assert matchEnd > 0
			assert matchAt < matchEnd
			this.matchAt = matchAt
			this.matchEnd = matchEnd
			this.hasMatch = true
			return this
		}

		TextSearchResult build() {
			return new TextSearchResult(this)
		}
	}
}
