package greenmoonsoftware.es.samples.docs.events;

import greenmoonsoftware.es.event.AbstractEvent;

public class LinesDeletedEvent extends AbstractEvent {
    private final int startIndex;
    private final int endIndex;

    public LinesDeletedEvent(String aggregateId, int startIndex, int endIndex) {
        super(aggregateId, "lines-deleted");
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public String toString() {
        return String.format("LinesDeleted: Aggregate=%s, startIndex=%d, endIndex=%d", getAggregateId(), startIndex, endIndex);
    }
}
