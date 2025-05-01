package com.kazurayam.ks.logging

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.apache.commons.lang.time.StopWatch

import java.time.Duration
import java.time.LocalDateTime
import java.time.Instant
import java.time.format.DateTimeFormatter

class SimplifiedStopWatch {

    private StopWatch stopWatch;

    SimplifiedStopWatch() {
        stopWatch = new StopWatch()
        stopWatch.start()
    }

    void stop() {
        stopWatch.stop()
    }

    String getStartTimeAsString() {
        return DateTimeFormatter.ISO_DATE_TIME.format(getStartTime())
    }

    LocalDateTime getStartTime() {
        return LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(stopWatch.getStartTime()),
                        TimeZone.getDefault().toZoneId())
    }

    LocalDateTime getEndTime() {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(stopWatch.getStartTime() + stopWatch.getTime()),
                TimeZone.getDefault().toZoneId())
    }

    Double getDurationSeconds() {
        Duration dur = Duration.between(getStartTime(), getEndTime())
        return dur.toMillis() / 1000
    }

    String toString() {
        return toJson()
    }

    String toJson() {
        ObjectMapper mapper = new ObjectMapper()
        SimpleModule module = new SimpleModule("SimplifiedStopWatchSerializer",
                new Version(1, 0, 0, null, null, null))
        module.addSerializer(SimplifiedStopWatch.class, new SimplifiedStopWatchSerializer())
        mapper.registerModules(module)
        return mapper.writeValueAsString(this)
    }

    static class SimplifiedStopWatchSerializer extends StdSerializer<SimplifiedStopWatch> {
        SimplifiedStopWatchSerializer() {
            this(null)
        }
        SimplifiedStopWatchSerializer(Class<SimplifiedStopWatch> t) {
            super(t)
        }
        @Override
        void serialize(SimplifiedStopWatch ssw,
                       JsonGenerator gen, SerializerProvider serializer) {
            gen.writeStartObject()
            gen.writeStringField("started at", ssw.getStartTimeAsString())
            gen.writeNumberField("duration seconds", ssw.getDurationSeconds())
            gen.writeEndObject()
        }
    }
}
