package greenmoonsoftware.es.samples.docs;

import greenmoonsoftware.es.event.Aggregate;
import greenmoonsoftware.es.event.EventApplier;
import greenmoonsoftware.es.event.EventList;
import greenmoonsoftware.es.samples.docs.events.DocumentCreatedEvent;
import greenmoonsoftware.es.samples.docs.events.LineChangedEvent;
import greenmoonsoftware.es.samples.docs.events.LinesDeletedEvent;
import greenmoonsoftware.es.samples.docs.events.LinesInsertedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Document implements Aggregate {
    private String aggregateId;
    private List<String> internal = new ArrayList<>();

    @Override
    public String getId() {
        return aggregateId;
    }

    public String getContent() {
        return internal.stream().collect(Collectors.joining("\n"));
    }

    @Override
    public void apply(EventList events) {
        events.forEach(event -> EventApplier.apply(this, event));
    }

    private void handle(DocumentCreatedEvent event) {
        aggregateId = event.getAggregateId();
        internal.addAll(Arrays.asList(event.getContent().split("\n")));
    }

    private void handle(LineChangedEvent event) {
        System.out.println("Handle: " + event);
        if (emptyLinesRequired(event.getLineNumber())) {
            populateEmptyLines(event.getLineNumber());
            internal.add(event.getNewLine());
        }
        else {
            internal.set(event.getLineNumber(), event.getNewLine());
        }
    }

    private boolean emptyLinesRequired(int lineNumber) {
        return internal.size() <= lineNumber;
    }

    private void populateEmptyLines(int upToLineNumber) {
        for (int i = internal.size(); i < upToLineNumber; i++) {
            internal.add("");
        }
    }

    private void handle(LinesInsertedEvent event) {
        System.out.println("Handle: "+event);
        internal.addAll(event.getStartLineNumber(), event.getLines());
    }

    private void handle(LinesDeletedEvent event) {
        System.out.println("Handle: "+event);
        internal.subList(event.getStartIndex(), event.getEndIndex() + 1).clear();
    }
}
