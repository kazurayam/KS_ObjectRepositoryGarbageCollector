package com.kazurayam.ks.text

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer

/**
 * DigestedText is a value-object,
 * is a list of DigestedLine objects, which is an representation of
 * a source file of Test Case script.
 */
class DigestedText {

	private List<DigestedLine> digestedLines

	DigestedText() {
		digestedLines = new ArrayList<>()
	}

	void add(DigestedLine line) {
		Objects.requireNonNull(line)
		digestedLines.add(line)
	}

	int size() {
		return digestedLines.size()
	}

	List<DigestedLine> getDigestedLines() {
		return this.digestedLines
	}

	String toJson() {
		ObjectMapper mapper = new ObjectMapper()
		SimpleModule module = new SimpleModule("DigestedTextSerializer",
				new Version(1, 0, 0, null, null, null))
		module.addSerializer(DigestedText.class, new DigestedTextSerializer())
		module.addSerializer(DigestedLine.class, new DigestedLine.DigestedLineSerializer())
		mapper.registerModules(module)
		return mapper.writeValueAsString(this)
	}

	static class DigestedTextSerializer extends StdSerializer<DigestedText> {
		DigestedTextSerializer() {
			this(null)
		}
		DigestedTextSerializer(Class<DigestedText> t) {
			super(t)
		}
		@Override
		void serialize(DigestedText dt,
				JsonGenerator gen, SerializerProvider serializer) {
			gen.writeStartArray()
			List<DigestedLine> lines = dt.getDigestedLines()
			lines.each { line ->
				gen.writeObject(line)
			}
			gen.writeEndArray()
		}
	}
}
