package greenmoonsoftware.tidewater.json.instant

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.node.LongNode

import java.time.Instant

class InstantDeserializer extends JsonDeserializer<Instant> {
    @Override
    Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        def node = p.readValueAsTree()
        def epochMillis = node.get('epochMillis') as LongNode
        Instant.ofEpochMilli(epochMillis.longValue())
    }
}
