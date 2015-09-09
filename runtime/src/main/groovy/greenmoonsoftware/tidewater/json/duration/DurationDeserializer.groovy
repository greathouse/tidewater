package greenmoonsoftware.tidewater.json.duration
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.node.IntNode

import java.time.Duration

class DurationDeserializer extends JsonDeserializer<Duration> {
    @Override
    Duration deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        def node = p.readValueAsTree()
        def millis = node.get('millis') as IntNode
        Duration.ofMillis(millis.longValue())
    }
}
