package com.kazurayam.ks.testobject.combine

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.kazurayam.ks.testobject.Locator

class RunDescription {

    private String projectName
    private Set<String> includeScriptsFolder
    private Set<String> includeObjectRepositoryFolder
    private int numberOfTestCases
    private int numberOfTestObjects
    private int numberOfUnusedTestObjects

    private RunDescription(Builder builder) {
        this.projectName = builder.projectName
        this.includeScriptsFolder = builder.includeScriptsFolder
        this.includeObjectRepositoryFolder = builder.includeObjectRepositoryFolder
        this.numberOfTestCases = builder.numberOfTestCases
        this.numberOfTestObjects = builder.numberOfTestObjects
        this.numberOfUnusedTestObjects = builder.numberOfUnusedTestObjects
    }

    static class Builder {
        private String projectName
        private Set<String> includeScriptsFolder
        private Set<String> includeObjectRepositoryFolder
        private int numberOfTestCases
        private int numberOfTestObjects
        private int numberOfUnusedTestObjects
        Builder(String projectName) {
            this.projectName = projectName
            includeScriptsFolder = new TreeSet<>()
            includeObjectRepositoryFolder = new TreeSet<>()
            numberOfTestCases = 0
            numberOfTestObjects = 0
            numberOfUnusedTestObjects = 0
        }
        Builder includeScriptsFolder(String... includeScriptsFolder) {
            this.includeScriptsFolder.addAll(includeScriptsFolder)
            return this
        }
        Builder includeObjectRepositoryFolder(String... includeObjectRepositoryFolder) {
            this.includeObjectRepositoryFolder.addAll(includeObjectRepositoryFolder)
            return this
        }
        Builder numberOfTestCases(int numberOfTestCases) {
            this.numberOfTestCases = numberOfTestCases
            return this
        }
        Builder numberOfTestObjects(int numberOfTestObjects) {
            this.numberOfTestObjects = numberOfTestObjects
            return this
        }
        Builder numberOfUnusedTestObjects(int numberOfUnusedTestObjects) {
            this.numberOfUnusedTestObjects = numberOfUnusedTestObjects
            return this
        }
        RunDescription build() {
            return new RunDescription(this)
        }
    }

    @Override
    String toString() {
        return toJson()
    }

    String toJson() {
        ObjectMapper mapper = new ObjectMapper()
        SimpleModule module = new SimpleModule("RunDescriptionSerializer",
                new Version(1, 0, 0, null, null, null))
        module.addSerializer(RunDescription.class, new RunDescriptionSerializer())
        mapper.registerModules(module)
        return mapper.writeValueAsString(this)
    }

    static class RunDescriptionSerializer extends StdSerializer<RunDescription> {
        RunDescriptionSerializer() {
            this(null)
        }
        RunDescriptionSerializer(Class<RunDescription> t) {
            super(t)
        }
        /**
         * {
         *     "Project name": "katalon",
         *     "includeScriptsFolder": [
         *         "main",
         *         "misc"
         *     ],
         *     "includeObjectRepositoryFolder": [
         *         "main",
         *         "misc"
         *     ],
         *     "Number of TestCases": 5,
         *     "Number of TestObjects": 16,
         *     "Number of unused TestObjects": 5
         * }
         */
        @Override
        void serialize(RunDescription rd,
                       JsonGenerator gen, SerializerProvider serializer) {
            gen.writeStartObject()
            gen.writeStringField("Project name", rd.projectName)
            gen.writeFieldName("includeScriptsFolder")
            gen.writeStartArray()
            rd.includeScriptsFolder.each { pattern ->
                gen.writeString(pattern)
            }
            gen.writeEndArray()
            gen.writeFieldName("includeObjectRepositoryFolder")
            gen.writeStartArray()
            rd.includeObjectRepositoryFolder.each { pattern ->
                gen.writeString(pattern)
            }
            gen.writeEndArray()
            gen.writeNumberField("Number of TestCases", rd.numberOfTestCases)
            gen.writeNumberField("Number of TestObjects", rd.numberOfTestObjects)
            gen.writeNumberField("Number of unused TestObjects", rd.numberOfUnusedTestObjects)
            gen.writeEndObject()
        }
    }
}
