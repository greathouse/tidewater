package greenmoonsoftware.es.event;

import java.time.Instant;
import java.util.UUID;

public abstract class AbstractEvent implements Event {
    private final UUID id = UUID.randomUUID();
    private final String aggregateId;
    private final String type;
    private final Instant eventDateTime = Instant.now();

    //Dummy constructor to enable reflection-based deserialization
    protected AbstractEvent() {
        aggregateId = "";
        type = "";
    }

    public AbstractEvent(String aggregateId, String type) {
        this.aggregateId = aggregateId;
        this.type = type;
    }

    @Override
    public final UUID getId() {
        return id;
    }

    @Override
    public final String getAggregateId() {
        return aggregateId;
    }

    @Override
    public final String getType() {
        return type;
    }

    @Override
    public final Instant getEventDateTime() {
        return eventDateTime;
    }
}
