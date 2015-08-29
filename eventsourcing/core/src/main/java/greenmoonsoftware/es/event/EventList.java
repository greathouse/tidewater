package greenmoonsoftware.es.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class EventList {
    private final List<Event> events = new ArrayList<>();

    public EventList() {

    }

    public EventList(Event event) {
        events.add(event);
    }

    public EventList add(Event event) {
        events.add(event);
        return this;
    }

    public void forEach(Consumer<Event> action) {
        events.forEach(action);
    }

    public Iterator iterator() {
        return Collections.unmodifiableList(events).iterator();
    }

    public EventList addAll(List<Event> e) {
        events.addAll(e);
        return this;
    }
}
