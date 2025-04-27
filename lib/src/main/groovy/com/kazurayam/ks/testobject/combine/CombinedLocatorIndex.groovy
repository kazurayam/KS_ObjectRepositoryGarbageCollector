package com.kazurayam.ks.testobject.combine

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.kazurayam.ks.testobject.Locator
import com.kazurayam.ks.testobject.LocatorDeclarations
import com.kazurayam.ks.testobject.TestObjectId

class CombinedLocatorIndex {

    private Map<Locator, Set<CombinedLocatorDeclarations>> map

    CombinedLocatorIndex() {
        this.map = new TreeMap<>()
    }

    Set<Locator> keySet() {
        return map.keySet()
    }

    Iterator<Map.Entry<Locator, Set<CombinedLocatorDeclarations>>> iterator() {
        return map.entrySet().iterator()
    }

    Set<CombinedLocatorDeclarations> get(Locator locator) {
        Objects.requireNonNull(locator)
        return map.get(locator)
    }

    void put(Locator locator, CombinedLocatorDeclarations cld) {
        Objects.requireNonNull(locator)
        // cld might be null
        if (!map.containsKey(locator)) {
            Set<CombinedLocatorDeclarations> emptySet = new TreeSet<>()
            map.put(locator, emptySet)
        }
        Set<CombinedLocatorDeclarations> set = map.get(locator)
        if (set != null) {
            if (cld != null) {
                set.add(cld)
            }
        }
    }

    void put(Locator locator, Set<CombinedLocatorDeclarations> cldSet) {
        Objects.requireNonNull(locator)
        // cldSet could be null
        if (!map.containsKey(locator)) {
            Set<CombinedLocatorDeclarations> emptySet = new TreeSet<>()
            map.put(locator, emptySet)
        }
        Set<CombinedLocatorDeclarations> set = map.get(locator)
        if (set != null) {
            if (cldSet != null) {
                set.addAll(cldSet)
            }
        }
    }

    Set<CombinedLocatorDeclarations> remove(Locator locator) {
        return map.remove(locator)
    }

    int size() {
        return map.size()
    }

    @Override
    String toString() {
        return this.toJson()
    }

    String toJson() {
        ObjectMapper mapper = new ObjectMapper()
        SimpleModule module = new SimpleModule("CombinedLocatorIndexSerializer",
                new Version(1, 0, 0, null, null, null))
        module.addSerializer(CombinedLocatorIndex.class, new CombinedLocatorIndexSerializer())
        module.addSerializer(CombinedLocatorDeclarations.class, new CombinedLocatorDeclarations.CombinedLocatorDeclarationsSerializer())
        module.addSerializer(Locator.class, new Locator.LocatorSerializer())
        module.addSerializer(LocatorDeclarations.class, new LocatorDeclarations.LocatorDeclarationsSerializer())
        module.addSerializer(TestObjectId.class, new TestObjectId.TestObjectIdSerializer())
        module.addSerializer(BackwardReferences.class, new BackwardReferences.BackwardReferencesSerializer())
        mapper.registerModule(module)
        return mapper.writeValueAsString(this)
    }

    static class CombinedLocatorIndexSerializer extends StdSerializer<CombinedLocatorIndex> {
        CombinedLocatorIndexSerializer() {
            this(null)
        }
        CombinedLocatorIndexSerializer(Class<CombinedLocatorIndex> t) {
            super(t)
        }
        @Override
        void serialize(CombinedLocatorIndex clx,
                       JsonGenerator gen, SerializerProvider provider) {
            gen.writeStartObject()
            gen.writeFieldName("CombinedLocatorIndex")
            gen.writeStartArray()
            Set<Locator> keys = clx.keySet()
            keys.each {locator ->
                gen.writeStartObject()
                gen.writeStringField("Locator", locator.getValue())
                Set<CombinedLocatorDeclarations> cldSet = clx.get(locator)
                gen.writeNumberField("Number of TestObjects that contain this Locator", cldSet.size())
                gen.writeFieldName("TestObjects")
                gen.writeStartArray()
                cldSet.each { cld ->
                    gen.writeObject(cld)
                }
                gen.writeEndArray()
                gen.writeEndObject()
            }
            gen.writeEndArray()
            gen.writeEndObject()
        }
    }
}
