package com.kazurayam.ks.testobject.combine

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.kazurayam.ks.testobject.Locator
import com.kazurayam.ks.testobject.TestObjectId

class CombinedLocatorDeclarations implements Comparable<CombinedLocatorDeclarations> {

    private TestObjectId testObjectId
    private Set<BackwardReferences> declarations

    CombinedLocatorDeclarations(TestObjectId testObjectId, Set<BackwardReferences> declarations) {
        Objects.requireNonNull(testObjectId)
        Objects.requireNonNull(declarations)
        this.testObjectId = testObjectId
        this.declarations = declarations
    }

    CombinedLocatorDeclarations(TestObjectId testObjectId) {
        this(testObjectId, new TreeSet<BackwardReferences>())
    }

    CombinedLocatorDeclarations(CombinedLocatorDeclarations that) {
        this(that.testObjectId)
        this.declarations = new TreeSet<>()
        that.getDeclarations().each { backwardReference ->
            this.declarations.add(backwardReference)
        }
    }

    TestObjectId getTestObjectId() {
        return this.testObjectId
    }

    Set<BackwardReferences> getDeclarations() {
        return this.declarations
    }

    void add(BackwardReferences declaration) {
        Objects.requireNonNull(declaration)
        declarations.add(declaration)
    }

    @Override
    boolean equals(Object obj) {
        if ( ! obj instanceof CombinedLocatorDeclarations) {
            return false
        }
        CombinedLocatorDeclarations that = (CombinedLocatorDeclarations)obj
        if (this.getTestObjectId() != that.getTestObjectId()) {
            return false
        }
        if (this.getDeclarations().size() != that.getDeclarations().size()) {
            return false
        }
        List<BackwardReferences> thisList = this.getDeclarations() as List
        List<BackwardReferences> thatList = that.getDeclarations() as List
        for (int i = 0; i < thisList.size(); i++) {
            if (thisList.get(i) != thatList.get(i)) {
                return false
            }
        }
        return true
    }

    @Override
    int hashCode() {
        return getTestObjectId().hashCode()
    }

    @Override
    int compareTo(CombinedLocatorDeclarations that) {
        int v = getTestObjectId().compareTo(that.getTestObjectId())
        if (v != 0) {
            return v
        } else {
            v = this.getDeclarations().size() - that.getDeclarations().size()
            if (v != 0) {
                return v
            } else {
                List<BackwardReferences> thisList = this.getDeclarations() as List
                List<BackwardReferences> thatList = that.getDeclarations() as List
                for (int i = 0; i < thisList.size(); i++) {
                    v = thisList.get(i).compareTo(thatList.get(i))
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
        SimpleModule module = new SimpleModule("CombinedLocatorDeclarationsSerializer",
                new Version(1, 0, 0, null, null, null))
        module.addSerializer(CombinedLocatorDeclarations.class, new CombinedLocatorDeclarationsSerializer())
        module.addSerializer(TestObjectId.class, new TestObjectId.TestObjectIdSerializer())
        module.addSerializer(BackwardReferences.class, new BackwardReferences.BackwardReferencesSerializer())
        mapper.registerModules(module)
        return mapper.writeValueAsString(this)
    }

    static class CombinedLocatorDeclarationsSerializer extends StdSerializer<CombinedLocatorDeclarations> {
        CombinedLocatorDeclarationsSerializer() {
            this(null)
        }
        CombinedLocatorDeclarationsSerializer(Class<CombinedLocatorDeclarations> t) {
            super(t)
        }
        @Override
        void serialize(CombinedLocatorDeclarations cld,
                       JsonGenerator gen, SerializerProvider serializer) {
            gen.writeStartObject()
            gen.writeStringField("TestObjectId", cld.getTestObjectId().getValue())
            gen.writeNumberField("Number of BackwardReferences", cld.getDeclarations().size())
            if (cld.getDeclarations().size() > 0) {
                gen.writeFieldName("BackwardReferences")
                gen.writeStartArray()
                cld.getDeclarations().each { br ->
                    gen.writeObject(br)
                }
                gen.writeEndArray()
            }
            gen.writeEndObject()
        }
    }
}
