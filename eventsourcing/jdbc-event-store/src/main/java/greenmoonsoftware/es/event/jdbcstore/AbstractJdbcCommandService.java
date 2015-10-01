package greenmoonsoftware.es.event.jdbcstore;

import greenmoonsoftware.es.Bus;
import greenmoonsoftware.es.command.AggregateCommandApplier;
import greenmoonsoftware.es.command.Command;
import greenmoonsoftware.es.event.Aggregate;
import greenmoonsoftware.es.event.Event;
import greenmoonsoftware.es.event.EventSubscriber;

import java.util.Collection;

public abstract class AbstractJdbcCommandService<T extends Aggregate> {
    protected final Bus<Event, EventSubscriber> eventBus;
    protected final JdbcStoreEventSubscriber eventStore;
    protected final JdbcStoreQuery<T> query;

    public AbstractJdbcCommandService(Bus<Event, EventSubscriber> b, JdbcStoreEventSubscriber eventStore, JdbcStoreQuery<T> q) {
        eventBus = b;
        this.eventStore = eventStore;
        query = q;
    }

    public final void execute(Command command) {
        T aggregate = query.retrieve(command.getAggregateId());
        Collection<Event> newEvents = AggregateCommandApplier.apply(aggregate, command);
        save(newEvents);
        raise(newEvents);
    }

    private void raise(Collection<Event> newEvents) {
        newEvents.stream().forEach(eventBus::post);
    }

    private void save(Collection<Event> newEvents) {
        newEvents.stream().forEach(eventStore::onEvent);
    }
}
