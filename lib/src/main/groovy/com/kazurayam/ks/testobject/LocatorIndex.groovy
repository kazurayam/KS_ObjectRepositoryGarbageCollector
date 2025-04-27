package com.kazurayam.ks.testobject

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer

/**
 * LocatorIndex is a key-values pair; the key is a Locator, the values is a set
 * of TestObjectId objects.
 * LocatorIndex makes it visible how a single instance of Locator is repeatedly
 * specified in which Test Objects, plus if each Test Object is referred to
 * by which Test Case script.
 */
class LocatorIndex {

	private Map<Locator, Set<LocatorDeclarations>> map

	LocatorIndex() {
		this.map = new TreeMap<>()
	}

	Set<Locator> keySet() {
		return map.keySet()
	}

	Iterator<Map.Entry<Locator, Set<LocatorDeclarations>>> iterator() {
		return map.entrySet().iterator()
	}

	Set<LocatorDeclarations> get(Locator locator) {
		Objects.requireNonNull(locator)
		return map.get(locator)
	}

	void put(Locator locator, LocatorDeclarations declarations) {
		Objects.requireNonNull(locator)
		Objects.requireNonNull(declarations)
		if (!map.containsKey(locator)) {
			Set<LocatorDeclarations> emptySet = new TreeSet<>()
			map.put(locator, emptySet)
		}
		Set<LocatorDeclarations> set = map.get(locator)
		if (set != null) {
			set.add(declarations)
		}
	}

	void put(Locator locator, Set<LocatorDeclarations> declarationsSet) {
		Objects.requireNonNull(locator)
		Objects.requireNonNull(declarationsSet)
		if (!map.containsKey(locator)) {
			Set<LocatorDeclarations> emptySet = new TreeSet<>()
			map.put(locator, emptySet)
		}
		Set<LocatorDeclarations> set = map.get(locator)
		if (set != null) {
			set.addAll(declarationsSet)
		}
	}

	/**
	 * Removes the mapping for a locator from this LocatorIndex if it is present
	 * (optional operation).
	 * @param locator
	 * @return the previous value of Set<LocatorDeclarations> associated with
	 * locator, or null if there was no mapping for locator.
	 */
	Set<LocatorDeclarations> remove(Locator locator) {
		return map.remove(locator)
	}

	/**
	 * @return number of Locators registered
	 */
	int size() {
		return map.size()
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
		module.addSerializer(LocatorDeclarations.class, new LocatorDeclarations.LocatorDeclarationsSerializer())
		module.addSerializer(TestObjectId.class, new TestObjectId.TestObjectIdSerializer())
		mapper.registerModule(module)
		return mapper.writeValueAsString(this)
	}

	static class LocatorIndexSerializer extends StdSerializer<LocatorIndex> {
		LocatorIndexSerializer() {
			this(null)
		}
		LocatorIndexSerializer(Class<LocatorIndex> t) {
			super(t)
		}
		@Override
		void serialize(LocatorIndex locatorIndex,
					   JsonGenerator gen, SerializerProvider serializer) {
			gen.writeStartObject()
			gen.writeFieldName("LocatorIndex")
			gen.writeStartArray()
			Set<Locator> keys = locatorIndex.keySet()
			keys.each { locator ->
				gen.writeStartObject()
				gen.writeObjectField("Locator", locator)
				Set<LocatorDeclarations> declarations = locatorIndex.get(locator)
				gen.writeNumberField("Number of TestObjects that contain this Locator", declarations.size())
				gen.writeFieldName("TestObjects")
				gen.writeStartArray()
				declarations.each { ld ->
					gen.writeObject(ld)
				}
				gen.writeEndArray()
				gen.writeEndObject()
			}
			gen.writeEndArray()
			gen.writeEndObject()
		}
	}
}