package greenmoonsoftware.tidewater.json

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.jdbcstore.EventSerializer
import greenmoonsoftware.tidewater.json.duration.DurationDeserializer
import greenmoonsoftware.tidewater.json.duration.DurationSerializer
import greenmoonsoftware.tidewater.json.instant.InstantDeserializer
import greenmoonsoftware.tidewater.json.instant.InstantSerializer
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDouble

import java.time.Duration
import java.time.Instant

class JsonEventSerializer implements EventSerializer<Event> {
    @Override
    InputStream serialize(Event event) throws IOException {
        def mapper = new ObjectMapper()
        mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.ANY)
        def module = new SimpleModule()
        module.addSerializer(Instant.class, new InstantSerializer())
        module.addSerializer(Duration.class, new DurationSerializer())
        mapper.registerModule(module)

        new ByteArrayInputStream(mapper.writeValueAsString(event).getBytes("UTF-8"))
    }

    @Override
    Event deserialize(String eventType, InputStream stream) throws IOException {
        def mapper = new ObjectMapper()
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)

        def module = new SimpleModule()
        module.addDeserializer(Instant.class, new InstantDeserializer())
        module.addDeserializer(Duration.class, new DurationDeserializer())
        module.addAbstractTypeMapping(Step, StepDouble)
        mapper.registerModule(module)

        def json = stream.text
        def event = mapper.readValue(json, Class.forName(eventType)) as Event
        return event
    }
}
