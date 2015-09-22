import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber

class IntegrationTestEventCollector implements EventSubscriber<Event> {
    private List<Event> events = []

    @Override
    void onEvent(Event event) {
        events << event
    }

    List<Event> getEvents() {
        events.asImmutable()
    }

    void clear() {
        events.clear()
    }
}
