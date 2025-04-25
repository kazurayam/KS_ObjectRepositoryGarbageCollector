package com.kazurayam.ks.testobject.combine

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.kazurayam.ks.testobject.TestObjectEssence
import com.kazurayam.ks.testobject.TestObjectId

/**
 * LocatorIndex is a key-values pair; the key is a Locator, the values is a set
 * of BackwardReference objects.
 * LocatorIndex makes it visible how a single instance of Locator is repeatedly
 * specified in which Test Objects, plus if each Test Object is referred to
 * by which Test Case script.
 */
class LocatorIndex {

	private Map<Locator, Set<BackwardReferences>> locatorIndex

	LocatorIndex() {
		this.locatorIndex = new TreeMap<>()
	}

	Set<Locator> keySet() {
		return locatorIndex.keySet()
	}

	Iterator<Map.Entry<Locator, Set<BackwardReferences>>> iterator() {
		return locatorIndex.entrySet().iterator()
	}

	Set<BackwardReferences> get(Locator locator) {
		Objects.requireNonNull(locator)
		return locatorIndex.get(locator)
	}

	void put(Locator locator, Set<BackwardReferences> backwardReferencesSet = null) {
		Objects.requireNonNull(locator)
		if (!locatorIndex.containsKey(locator)) {
			Set<BackwardReferences> emptySet = new TreeSet<>()
			locatorIndex.put(locator, emptySet)
		}
		Set<BackwardReferences> set = locatorIndex.get(locator)
		if (backwardReferencesSet != null) {
			set.addAll(backwardReferencesSet)
		}
	}

	/**
	 * Removes the mapping for a locator from this LocatorIndex if it is present (optional operation).
	 * @param locator
	 * @return the previous value of Set<BackwardReferences> associated with locator, or null if there was no mapping for locator.
	 */
	Set<BackwardReferences> remove(Locator locator) {
		return locatorIndex.remove(locator)
	}

	/**
	 * @return number of Locators registered
	 */
	int size() {
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
		module.addSerializer(BackwardReferences.class, new BackwardReferences.BackwardReferencesSerializer())
		module.addSerializer(ForwardReference.class, new ForwardReference.ForwardReferenceSerializer())
		module.addSerializer(TestObjectEssence.class, new TestObjectEssence.TestObjectEssenceSerializer())
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
		void serialize(LocatorIndex locatorIndex, JsonGenerator gen, SerializerProvider serializer) {
			gen.writeStartObject()
			gen.writeFieldName("LocatorIndex")
			gen.writeStartArray()
			Set<Locator> keys = locatorIndex.keySet()
			keys.each { locator ->
				gen.writeStartObject()
				gen.writeStringField("Locator", locator.getValue())
				Set<BackwardReferences> backwardReferencesSet = locatorIndex.get(locator)
				gen.writeNumberField("Number of TestObjects containing this Locator", backwardReferencesSet.size())
				gen.writeFieldName("TestObjects")
				gen.writeStartArray()
				backwardReferencesSet.each { br ->
					gen.writeObject(br)
				}
				gen.writeEndArray()
				gen.writeEndObject()
			}
			gen.writeEndArray()
			gen.writeEndObject()
		}
	}
}