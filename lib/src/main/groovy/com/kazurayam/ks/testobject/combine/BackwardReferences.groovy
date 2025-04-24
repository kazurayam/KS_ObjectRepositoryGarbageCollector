package com.kazurayam.ks.testobject.combine

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.kazurayam.ks.testobject.TestObjectEssence
import com.kazurayam.ks.testobject.TestObjectId

class BackwardReferences {

    private TestObjectEssence testObjectEssence
    private Set<ForwardReference> forwardReferences

    BackwardReferences(TestObjectEssence testObjectEssence,
                       Set<ForwardReference> forwardReferences) {
        Objects.requireNonNull(testObjectEssence)
        Objects.requireNonNull(forwardReferences)
        this.testObjectEssence = testObjectEssence
        this.forwardReferences = forwardReferences
    }

    /**
     * Copy constructor
     */
    BackwardReferences(BackwardReferences that) {
        this.testObjectEssence = new TestObjectEssence(that.getTestObjectEssence())
        this.forwardReferences = new TreeSet<>()
        that.getForwardReferences().each { ForwardReference ref ->
            forwardReferences.add(new ForwardReference(ref))
        }
    }

    TestObjectId getTestObjectId() {
        return testObjectEssence.getTestObjectId()
    }

    TestObjectEssence getTestObjectEssence() {
        return testObjectEssence
    }

    Set<ForwardReference> getForwardReferences() {
        return forwardReferences
    }

    int getNumberOfReferences() {
        return forwardReferences.size()
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
        module.addSerializer(TestObjectEssence.class, new TestObjectEssence.TestObjectEssenceSerializer())
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
            gen.writeObjectField("TestObjectEssence", br.getTestObjectEssence())
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
