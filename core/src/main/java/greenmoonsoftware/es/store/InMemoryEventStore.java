package greenmoonsoftware.es.store;

import greenmoonsoftware.es.event.Aggregate;
import greenmoonsoftware.es.event.Event;
import greenmoonsoftware.es.event.EventList;
import greenmoonsoftware.es.event.EventSubscriber;

import java.util.HashMap;
import java.util.Map;

public abstract class InMemoryEventStore<T extends Aggregate> implements EventSubscriber<Event>, StoreRetrieval<T> {
    private Map<String, EventList> eventsByAggregate = new HashMap<>();

    protected abstract T create();

    @Override
    public void onEvent(Event event) {
        System.out.println("InMemoryEventStore::onEvent: "+event);
        eventsByAggregate.compute(
                event.getAggregateId(),
                (k,v) -> v == null ? new EventList(event) : v.add(event)
        );
    }

    @Override
    public T retrieve(String aggregateId) {
        System.out.println("InMemoryEventStore::retrieve: "+aggregateId);
        T aggregate = create();
        aggregate.apply(eventsByAggregate.getOrDefault(aggregateId, new EventList()));
        return aggregate;
    }

}
