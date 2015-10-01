package greenmoonsoftware.tidewater.json.contextId
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.node.TextNode
import greenmoonsoftware.tidewater.context.ContextId

class ContextIdDeserializer extends JsonDeserializer<ContextId> {
    @Override
    ContextId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        def node = p.readValueAsTree()
        def value = node.get('contextId') as TextNode
        new ContextId(value.asText())
    }
}
