package greenmoonsoftware.es.samples.docs.events;

import greenmoonsoftware.es.event.Event;

import java.time.Instant;
import java.util.UUID;

public final class LineChangedEvent implements Event {
    private final UUID id = UUID.randomUUID();
    private final String aggregateId;
    private final String type = "line-changed";
    private final Instant eventDateTime = Instant.now();
    private final int lineNumber;
    private final String newLine;

    public LineChangedEvent(String aggregateId, int lineNumber, String newLine) {
        this.aggregateId = aggregateId;
        this.lineNumber = lineNumber;
        this.newLine = newLine;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Instant getEventDateTime() {
        return eventDateTime;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getNewLine() {
        return newLine;
    }

    public String toString() {
        return String.format("LineChanged: Aggregate=%s, lineNumber=%d, newLine=%s", aggregateId, lineNumber, newLine);
    }
}
