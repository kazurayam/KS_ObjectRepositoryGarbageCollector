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
        LocatorDeclarations other = (LocatorDeclarations)obj
        if (this.getLocator() != other.getLocator()) {
            return false
        }
        // TODO:
        //  Should I check the equality of this.getDeclarations() and other.getDeclarations()?
        //  or not necessary? ... possibly not
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
        SimpleModule module = new SimpleModule("LocatorDeclrationsSerializer",
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

