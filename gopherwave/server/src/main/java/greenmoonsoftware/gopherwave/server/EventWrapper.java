package greenmoonsoftware.gopherwave.server;

import greenmoonsoftware.es.event.Event;

import java.time.Instant;
import java.util.UUID;

public class EventWrapper implements Event {
    private final Event internal;

    private final UUID handlerId;
    public EventWrapper(Event internal, UUID handlerId) {
        this.internal = internal;
        this.handlerId = handlerId;
    }

    public Event getEvent() {
        return internal;
    }

    @Override
    public UUID getId() {
        return internal.getId();
    }

    @Override
    public String getAggregateId() {
        return internal.getAggregateId();
    }

    @Override
    public String getType() {
        return internal.getType();
    }

    @Override
    public Instant getEventDateTime() {
        return internal.getEventDateTime();
    }

    public UUID getHandlerId() {
        return handlerId;
    }
}
