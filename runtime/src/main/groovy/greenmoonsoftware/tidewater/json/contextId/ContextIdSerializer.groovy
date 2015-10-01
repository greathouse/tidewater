package greenmoonsoftware.tidewater.json.contextId
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import greenmoonsoftware.tidewater.context.ContextId

class ContextIdSerializer extends JsonSerializer<ContextId> {
    @Override
    void serialize(ContextId value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject()
        gen.writeStringField('contextId', value.id)
        gen.writeEndObject()
    }
}
