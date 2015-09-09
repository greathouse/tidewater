package greenmoonsoftware.tidewater.json.instant

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

import java.time.Instant

class InstantSerializer extends JsonSerializer<Instant> {
    @Override
    void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject()
        gen.writeNumberField('epochMillis', value.toEpochMilli())
        gen.writeEndObject()
    }
}
