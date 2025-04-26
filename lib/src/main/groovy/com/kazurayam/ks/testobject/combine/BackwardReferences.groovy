package com.kazurayam.ks.testobject.combine

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.kazurayam.ks.testobject.TestObjectId

class BackwardReferences implements Comparable<BackwardReferences> {

    private TestObjectId testObjectId
    private Set<ForwardReference> forwardReferences

    BackwardReferences(TestObjectId testObjectId) {
        Objects.requireNonNull(testObjectId)
        this.testObjectId = testObjectId
        this.forwardReferences = new TreeSet<>()
    }

    /**
     * Copy constructor
     */
    BackwardReferences(BackwardReferences that) {
        this.testObjectId = that.getTestObjectId()
        this.forwardReferences = new TreeSet<>()
        that.getForwardReferences().each { ForwardReference ref ->
            forwardReferences.add(new ForwardReference(ref))
        }
    }

    void add(ForwardReference value) {
        forwardReferences.add(value)
    }

    void addAll(Set<ForwardReference> values) {
        forwardReferences.addAll(values)
    }

    TestObjectId getTestObjectId() {
        return testObjectId
    }

    Set<ForwardReference> getForwardReferences() {
        return forwardReferences
    }

    int getNumberOfReferences() {
        return forwardReferences.size()
    }

    @Override
    boolean equals(Object obj) {
        if (!(obj instanceof BackwardReferences)) {
            return false
        }
        BackwardReferences other = (BackwardReferences) obj
        if (this.testObjectId != other.testObjectId) {
            return false
        }
        if (this.getNumberOfReferences() != other.getNumberOfReferences()) {
            return false
        }
        List<ForwardReference> theForwardReferences = this.getForwardReferences() as List
        List<ForwardReference> otherForwardReferences = other.getForwardReferences() as List
        for (int i = 0; i < theForwardReferences.size(); i++) {
            ForwardReference theFR = theForwardReferences.get(i)
            ForwardReference otherFR = otherForwardReferences.get(i)
            if (theFR != otherFR) {
                return false
            }
        }
        return true
    }

    @Override
    int hashCode() {
        int hash = 7;
        hash = 31 * hash + testObjectId.hashCode()
        forwardReferences.each { fr ->
            hash = 31 * hash + fr.hashCode()
        }
        return hash
    }

    @Override
    int compareTo(BackwardReferences other) {
        int v = this.testObjectId.compareTo(other.testObjectId)
        if (v != 0) {
            return v
        } else {
            v = this.forwardReferences.size() - other.forwardReferences.size()
            if (v != 0) {
                return v
            } else {
                List<ForwardReference> theFRList = this.forwardReferences as List
                List<ForwardReference> otherFRList = other.forwardReferences as List
                for (int i = 0; i < theFRList.size(); i++) {
                    ForwardReference theFR = theFRList.get(i)
                    ForwardReference otherFR = otherFRList.get(i)
                    v = theFR.compareTo(otherFR)
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
        return toJson()
    }

    String toJson() {
        ObjectMapper mapper = new ObjectMapper()
        SimpleModule module = new SimpleModule("BackwardReferencesSerializer",
                new Version(1, 0, 0, null, null, null))
        module.addSerializer(BackwardReferences.class, new BackwardReferencesSerializer())
        module.addSerializer(TestObjectId.class, new TestObjectId.TestObjectIdSerializer())
        module.addSerializer(ForwardReference.class, new ForwardReference.ForwardReferenceSerializer())
        mapper.registerModule(module)
        return mapper.writeValueAsString(this)
    }

    static class BackwardReferencesSerializer extends StdSerializer<BackwardReferences> {
        BackwardReferencesSerializer() {
            this(null)
        }

        BackwardReferencesSerializer(Class<BackwardReferences> t) {
            super(t)
        }
        @Override
        void serialize(BackwardReferences br,
                       JsonGenerator gen, SerializerProvider serializer) {
            gen.writeStartObject()
            gen.writeObjectField("TestObjectId", br.getTestObjectId())
            gen.writeNumberField("Number of references", br.getNumberOfReferences())
            gen.writeFieldName("ForwardReferences")
            gen.writeStartArray()
            br.getForwardReferences().each { fr ->
                gen.writeObject(fr)
            }
            gen.writeEndArray()
            gen.writeEndObject()
        }
    }
}
