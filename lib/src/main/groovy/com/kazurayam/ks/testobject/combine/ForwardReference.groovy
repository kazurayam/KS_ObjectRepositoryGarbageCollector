package com.kazurayam.ks.testobject.combine

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.kazurayam.ks.testcase.DigestedLine
import com.kazurayam.ks.testcase.TestCaseId
import com.kazurayam.ks.testobject.TestObjectId

/**
 * A ForwardReference object records a fact that a single TestCase refers to a single TestObject. 
 */
public class ForwardReference implements Comparable<ForwardReference> {

	private TestCaseId testCaseId
	private DigestedLine digestedLine
	private TestObjectId testObjectId

	ForwardReference(TestCaseId testCaseId, DigestedLine digestedLine, TestObjectId testObjectId) {
		Objects.requireNonNull(testCaseId)
		Objects.requireNonNull(digestedLine)
		Objects.requireNonNull(testObjectId)
		this.testCaseId = testCaseId
		this.digestedLine = digestedLine
		this.testObjectId = testObjectId
	}

	/**
	 * Copy constructor
	 */
	ForwardReference(ForwardReference that) {
		this(that.getTestCaseId(),
				new DigestedLine(that.getDigestedLine()),
				new TestObjectId(that.getTestObjectId())
		)
	}

	TestCaseId getTestCaseId() {
		return testCaseId
	}

	DigestedLine getDigestedLine() {
		return digestedLine
	}

	TestObjectId getTestObjectId() {
		return testObjectId
	}

	@Override
	boolean equals(Object obj) {
		if (!(obj instanceof ForwardReference)) {
			return false
		}
		ForwardReference other = (ForwardReference)obj
		return this.testCaseId == other.testCaseId &&
				this.digestedLine == other.digestedLine &&
				this.testObjectEssence == other.testObjectEssence
	}

	@Override
	int hashCode() {
		int hash = 7;
		hash = 31 * hash + testCaseId.hashCode()
		hash = 31 * hash + digestedLine.hashCode()
		hash = 31 * hash + testObjectId.hashCode()
		return hash;
	}

	@Override
	int compareTo(ForwardReference other) {
		int v = this.testCaseId.compareTo(other.testCaseId)
		if (v != 0) {
			return v
		} else {
			v = this.digestedLine.compareTo(other.digestedLine)
			if (v != 0) {
				return v
			} else {
				return this.testObjectId.compareTo(other.testObjectId)
			}
		}
	}

	@Override
	String toString() {
		return toJson()
	}

	String toJson() {
		ObjectMapper mapper = new ObjectMapper()
		SimpleModule module = new SimpleModule("ForwardReferenceSerializer",
				new Version(1, 0, 0, null, null, null))
		module.addSerializer(ForwardReference.class, new ForwardReferenceSerializer())
		module.addSerializer(TestCaseId.class, new TestCaseId.TestCaseIdSerializer())
		module.addSerializer(DigestedLine.class, new DigestedLine.DigestedLineSerializer())
		module.addSerializer(TestObjectId.class, new TestObjectId.TestObjectIdSerializer())
		mapper.registerModule(module)
		return mapper.writeValueAsString(this)
	}

	static class ForwardReferenceSerializer extends StdSerializer<ForwardReference> {
		ForwardReferenceSerializer() {
			this(null)
		}
		ForwardReferenceSerializer(Class<ForwardReferenceSerializer> t) {
			super(t)
		}
		@Override
		void serialize(ForwardReference fref,
				JsonGenerator gen, SerializerProvider serializer) {
			gen.writeStartObject()
			gen.writeObjectField("TestCaseId", fref.getTestCaseId())
			gen.writeObjectField("DigestedLine", fref.getDigestedLine())
			gen.writeObjectField("TestObjectId", fref.getTestObjectId())
			gen.writeEndObject()
		}
	}
}
