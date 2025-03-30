package com.kazurayam.ks.testcase

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer

import groovy.json.JsonOutput

public class DigestedLine implements Comparable<DigestedLine> {

	private String line
	private int lineNo
	//
	private String pattern
	private Boolean isRegex
	//
	private int matchAt
	private int matchEnd
	private Boolean hasMatch

	private DigestedLine(Builder builder) {
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

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DigestedLine))	{
			return false
		}
		DigestedLine other = (DigestedLine)obj
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
	int compareTo(DigestedLine other) {
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

	String toJson() {
		ObjectMapper mapper = new ObjectMapper()
		SimpleModule module = new SimpleModule("TextSearchResultSerializer",
				new Version(1, 0, 0, null, null, null))
		module.addSerializer(DigestedLine.class, new DigestedLineSerializer())
		mapper.registerModules(module)
		return mapper.writeValueAsString(this)
	}

	static class DigestedLineSerializer extends StdSerializer<DigestedLine> {
		DigestedLineSerializer() {
			this(null)
		}
		DigestedLineSerializer(Class<DigestedLineSerializer> t) {
			super(t)
		}
		@Override
		void serialize(DigestedLine tsr,
				JsonGenerator gen, SerializerProvider serializer) {
			gen.writeStartObject()
			gen.writeNumberField("lineNo", tsr.lineNo)
			gen.writeStringField("line", tsr.line)
			gen.writeStringField("pattern", tsr.pattern)
			gen.writeBooleanField("isRegex", tsr.isRegex)
			gen.writeBooleanField("hasMatch", tsr.hasMatch)
			gen.writeNumberField("matchAt", tsr.matchAt)
			gen.writeNumberField("matchEnd", tsr.matchEnd)
			gen.writeEndObject()
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

		DigestedLine build() {
			return new DigestedLine(this)
		}
	}
}
