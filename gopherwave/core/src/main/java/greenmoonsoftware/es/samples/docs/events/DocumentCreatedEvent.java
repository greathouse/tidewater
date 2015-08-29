package greenmoonsoftware.es.samples.docs.events;
import greenmoonsoftware.es.event.AbstractEvent;

public class DocumentCreatedEvent extends AbstractEvent {
    private final String content;

    public DocumentCreatedEvent(String aggregateId, String content) {
        super(aggregateId, "created");
        this.content = content;
    }
    public DocumentCreatedEvent(String aggregateId) {
        this(aggregateId, "");
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return this.getClass() + "{ aggregateId: \"" + getAggregateId() + "\", content.length: " + content.length() + "}";
    }
}
