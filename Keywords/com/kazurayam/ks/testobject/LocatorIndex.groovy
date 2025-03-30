package com.kazurayam.ks.testobject

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer

public class LocatorIndex {

	private Map<Locator, Set<TestObjectEssence>> locatorIndex

	LocatorIndex() {
		this.locatorIndex = new TreeMap<>()
	}

	Set<Locator> keySet() {
		return locatorIndex.keySet()
	}

	Iterator<Map.Entry<Locator, Set<TestObjectEssence>>> iterator() {
		return locatorIndex.entrySet().iterator()
	}

	Set<TestObjectEssence> get(Locator locator) {
		Objects.requireNonNull(locator)
		return locatorIndex.get(locator)
	}

	void put(Locator locator, TestObjectEssence essence) {
		Objects.requireNonNull(locator)
		Objects.requireNonNull(essence)
		if (!locatorIndex.containsKey(locator)) {
			Set<TestObjectEssence> emptySet = new TreeSet<>()
			locatorIndex.put(locator, emptySet)
		}
		Set<TestObjectEssence> set = locatorIndex.get(locator)
		set.add(essence)
	}

	public int size() {
		return locatorIndex.size()
	}

	@Override
	String toString() {
		return this.toJson()
	}

	String toJson() {
		ObjectMapper mapper = new ObjectMapper()
		SimpleModule module = new SimpleModule("LocatorIndexSerializer",
				new Version(1, 0, 0, null, null, null))
		module.addSerializer(LocatorIndex.class, new LocatorIndexSerializer())
		module.addSerializer(Locator.class, new Locator.LocatorSerializer())
		module.addSerializer(TestObjectEssence.class, new TestObjectEssence.TestObjectEssenceSerializer())
		module.addSerializer(TestObjectId.class, new TestObjectId.TestObjectIdSerializer())
		mapper.registerModule(module)
		return mapper.writeValueAsString(this)
	}

	static class LocatorIndexSerializer extends StdSerializer<LocatorIndex> {
		LocatorIndexSerializer() {
			this(null)
		}
		LocatorIndexSerializer(Class<LocatorIndexSerializer> t) {
			super(t)
		}
		@Override
		void serialize(LocatorIndex locatorIndex, JsonGenerator gen, SerializerProvider serializer) {
			gen.writeStartObject()
			gen.writeFieldName("LocatorIndex")
			gen.writeStartArray()
			Set<Locator> keys = locatorIndex.keySet()
			keys.each { locator ->
				gen.writeStartObject()
				gen.writeStringField("Locator", locator.value())
				gen.writeFieldName("TestObjectEssences")
				gen.writeStartArray()
				Set<TestObjectEssence> essences = locatorIndex.get(locator)
				essences.each { essence ->
					gen.writeObject(essence)
				}
				gen.writeEndArray()
				gen.writeEndObject()
			}
			gen.writeEndArray()
			gen.writeEndObject()
		}
	}
}