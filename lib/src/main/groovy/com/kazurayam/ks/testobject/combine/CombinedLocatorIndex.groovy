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

    private Map<Locator, Set<CombinedLocatorDeclarations>> index

    CombinedLocatorIndex() {
        this.index = new TreeMap<>()
    }

    Set<Locator> keySet() {
        return index.keySet()
    }

    Iterator<Map.Entry<Locator, Set<CombinedLocatorDeclarations>>> iterator() {
        return index.entrySet().iterator()
    }

    Set<CombinedLocatorDeclarations> get(Locator locator) {
        Objects.requireNonNull(locator)
        return index.get(locator)
    }

    void put(Locator locator, Set<CombinedLocatorDeclarations> cldSet) {
        Objects.requireNonNull(locator)
        Objects.requireNonNull(cldSet)
        index.put(locator, cldSet)
    }

    void put(Locator locator, CombinedLocatorDeclarations cld) {
        Objects.requireNonNull(locator)
        Objects.requireNonNull(cld)
        if (!index.containsKey(locator)) {
            index.put(locator, new TreeSet<CombinedLocatorDeclarations>())
        }
        Set<CombinedLocatorDeclarations> set = index.get(locator)
        set.add(cld)
    }

    Set<CombinedLocatorDeclarations> remove(Locator locator) {
        return index.remove(locator)
    }

    int size() {
        return index.size()
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
        module.addSerializer(BackwardReference.class, new BackwardReference.BackwardReferencesSerializer())
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
            int count = 0
            clx.iterator().each {entry ->
                if (isSuspiciousEntry(entry)) {
                    count += 1
                }
            }
            gen.writeNumberField("Number of Locators", clx.size())
            gen.writeNumberField("Number of Suspicious Locators", count)
            gen.writeFieldName("Locators")
            gen.writeStartArray()
            Set<Locator> keys = clx.keySet()
            keys.each {locator ->
                gen.writeStartObject()
                gen.writeObjectField("Locator", locator)
                Set<CombinedLocatorDeclarations> cldSet = clx.get(locator)
                gen.writeNumberField("Number of Container TestObjects", cldSet.size())
                if (cldSet.size() > 0) {
                    gen.writeFieldName("Container TestObjects")
                    gen.writeStartArray()
                    cldSet.each { cld ->
                        gen.writeObject(cld)
                    }
                    gen.writeEndArray()
                }
                gen.writeEndObject()
            }
            gen.writeEndArray()
            gen.writeEndObject()
        }
    }

    /**
     * A sort of copy constructor which chooses the suspicious locators
     * out of the source while dropping the trustworthy locators
     *
     * @param source
     * @return another CombinedLocatorIndex that contains suspicious locators only
     */
    static CombinedLocatorIndex suspect(CombinedLocatorIndex source) {
        CombinedLocatorIndex target = new CombinedLocatorIndex()
        source.iterator().each { entry ->
            if (isSuspiciousEntry(entry)) {
                target.put(entry.key, entry.value)
            }
        }
        return target
    }

    private static boolean isSuspiciousEntry(Map.Entry<Locator,
            Set<CombinedLocatorDeclarations>> entry) {
        boolean suspicious = false
        Set<CombinedLocatorDeclarations> cldSet = entry.value
        if (cldSet.size() == 0) {
            suspicious = true
        }
        cldSet.each {cld ->
            if (cld.getDeclarations().size() == 0) {
                suspicious = true
            }
        }
        return suspicious
    }
}
