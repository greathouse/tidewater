package greenmoonsoftware.es.event;

import greenmoonsoftware.es.Bus;

import java.util.ArrayList;
import java.util.List;

public class SimpleEventBus implements Bus<Event, EventSubscriber> {
    private List<EventSubscriber> subscribers = new ArrayList<>();

    @Override
    public void post(Event event) {
        subscribers.forEach(s -> s.onEvent(event));
    }

    @Override
    public Bus<Event, EventSubscriber> register(EventSubscriber subscriber) {
        subscribers.add(subscriber);
        return this;
    }

    @Override
    public void unregister(EventSubscriber subscriber) {
        subscribers.remove(subscriber);
    }
}
