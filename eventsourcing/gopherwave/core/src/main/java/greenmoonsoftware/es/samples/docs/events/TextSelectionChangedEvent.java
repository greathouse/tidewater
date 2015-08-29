package greenmoonsoftware.es.samples.docs.events;

import greenmoonsoftware.es.event.AbstractEvent;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class TextSelectionChangedEvent extends AbstractEvent {
    private final Set<Range> ranges = new HashSet<>();

    public TextSelectionChangedEvent(String aggregateId) {
        super(aggregateId, "selection-changed");
    }

    public Set<Range> getRanges() {
        return Collections.unmodifiableSet(ranges);
    }

    public TextSelectionChangedEvent addRange(int start, int end) {
        ranges.add(new Range(start, end));
        return this;
    }

    public static class Range implements Serializable {
        public final int start;
        public final int end;

        public Range(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
}
