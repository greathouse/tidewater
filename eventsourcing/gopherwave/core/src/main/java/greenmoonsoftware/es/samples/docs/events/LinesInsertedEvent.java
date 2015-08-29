package greenmoonsoftware.es.samples.docs.events;

import greenmoonsoftware.es.event.AbstractEvent;

import java.util.List;
import java.util.stream.Collectors;

public final class LinesInsertedEvent extends AbstractEvent {
    private final int startLineNumber;
    private final List<String> lines;

    public LinesInsertedEvent(String aggregateId, int startLineNumber, List<String> lines) {
        super(aggregateId, "lines-inserted");
        this.startLineNumber = startLineNumber;
        this.lines = lines;
    }

    public int getStartLineNumber() {
        return startLineNumber;
    }

    public List<String> getLines() {
        return lines;
    }

    public String toString() {
        return String.format("LinesInserted: Aggregate=%s, lineNumber=%d, totalLines=%d, lines=%s", getAggregateId(), startLineNumber, lines.size(), "[" + lines.stream().collect(Collectors.joining(",")) + "]");
    }
}
