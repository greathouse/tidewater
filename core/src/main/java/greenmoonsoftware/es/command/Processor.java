package greenmoonsoftware.es.command;

import greenmoonsoftware.es.Bus;
import greenmoonsoftware.es.event.Aggregate;
import greenmoonsoftware.es.event.Event;
import greenmoonsoftware.es.event.EventSubscriber;
import greenmoonsoftware.es.store.StoreRetrieval;

import java.util.Collection;

public class Processor<T extends Aggregate> {
    private final StoreRetrieval<T> store;
    private final Bus<Event, EventSubscriber> eventBus;

    public Processor(StoreRetrieval<T> store, Bus<Event, EventSubscriber> eventBus) {
        this.store = store;
        this.eventBus = eventBus;
    }

    public void process(Command command) {
        Aggregate aggregate = store.retrieve(command.getAggregateId());
        Collection<Event> newEvents = AggregateCommandApplier.apply(aggregate, command);
        newEvents.forEach(eventBus::post);
    }
}
