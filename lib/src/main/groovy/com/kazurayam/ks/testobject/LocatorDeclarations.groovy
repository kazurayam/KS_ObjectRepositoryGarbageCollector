package com.kazurayam.ks.testobject

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer

class LocatorDeclarations implements Comparable<LocatorDeclarations>{

    private Locator locator
    private Set<TestObjectId> declarations

    LocatorDeclarations(Locator locator, Set<TestObjectId> declarations) {
        Objects.requireNonNull(locator)
        Objects.requireNonNull(declarations)
        this.locator = locator
        this.declarations = declarations
    }

    LocatorDeclarations(Locator locator) {
        this(locator, new TreeSet<TestObjectId>())
    }

    Locator getLocator() {
        return this.locator
    }

    Set<TestObjectId> getDeclarations() {
        return this.declarations
    }

    /**
     * copy constructor
     * @param that
     */
    LocatorDeclarations(LocatorDeclarations that) {
        this(that.getLocator())
        this.declarations = new TreeSet<>()
        that.getDeclarations().each { testObjectId ->
            this.declarations.add(testObjectId)
        }
    }

    void add(TestObjectId testObjectId) {
        Objects.requireNonNull(testObjectId)
        declarations.add(testObjectId)
    }

    @Override
    boolean equals(Object obj) {
        if (!obj instanceof LocatorDeclarations) {
            return false
        }
        LocatorDeclarations that = (LocatorDeclarations)obj
        if (this.getLocator() != that.getLocator()) {
            return false
        }
        if (this.getDeclarations().size() != that.getDeclarations().size()) {
            return false
        }
        List<TestObjectId> thisList = this.getDeclarations() as List
        List<TestObjectId> thatList = that.getDeclarations() as List
        for (int i = 0; i < thisList.size(); i++) {
            if (thisList.get(i) != thatList.get(i)) {
                return false
            }
        }
        return true
    }

    @Override
    int hashCode() {
        return this.locator.hashCode()
    }

    @Override
    int compareTo(LocatorDeclarations that) {
        int v = this.locator.compareTo(that.locator)
        if (v != 0) {
            return v
        } else {
            v = this.declarations.size() - that.declarations.size()
            if (v != 0) {
                return v
            } else {
                List<TestObjectId> thisDeclarations = this.getDeclarations()
                        .toList().sort()
                List<TestObjectId> thatDeclarations = that.getDeclarations()
                        .toList().sort()
                for (int i = 0; i < thisDeclarations.size(); i++) {
                    TestObjectId thisTOI = thisDeclarations.get(i)
                    TestObjectId thatTOI = thatDeclarations.get(i)
                    v = thisTOI <=> thatTOI
                    if (v != 0) {
                        return v
                    }
                }
                return 0
            }
        }
    }

    @Override
    String toString() {
        return this.toJson()
    }

    String toJson() {
        ObjectMapper mapper = new ObjectMapper()
        SimpleModule module = new SimpleModule("LocatorDeclarationsSerializer",
                new Version(1, 0, 0, null, null, null))
        module.addSerializer(LocatorDeclarations.class, new LocatorDeclarationsSerializer())
        module.addSerializer(TestObjectId.class, new TestObjectId.TestObjectIdSerializer())
        mapper.registerModules(module)
        return mapper.writeValueAsString(this)
    }

    static class LocatorDeclarationsSerializer extends StdSerializer<LocatorDeclarations> {
        LocatorDeclarationsSerializer() {
            this(null)
        }
        LocatorDeclarationsSerializer(Class<LocatorDeclarations> t) {
            super(t)
        }
        @Override
        void serialize(LocatorDeclarations ld,
                       JsonGenerator gen, SerializerProvider serializer) {
            gen.writeStartObject()
            gen.writeObjectField("Locator", ld.getLocator())
            gen.writeNumberField("Number of container TestObjects", ld.getDeclarations().size())
            gen.writeFieldName("TestObjectIds")
            gen.writeStartArray()
            ld.getDeclarations().each { toi ->
                gen.writeString(toi.toString())
            }
            gen.writeEndArray()
            gen.writeEndObject()
        }
    }
}

