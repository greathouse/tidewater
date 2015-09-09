package greenmoonsoftware.tidewater.json.duration
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

import java.time.Duration

class DurationSerializer extends JsonSerializer<Duration> {
    @Override
    void serialize(Duration value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject()
        gen.writeNumberField('millis', value.toMillis())
        gen.writeEndObject()
    }
}
