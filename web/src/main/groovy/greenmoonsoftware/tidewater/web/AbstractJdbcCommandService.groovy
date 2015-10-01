package greenmoonsoftware.tidewater.web
import greenmoonsoftware.es.Bus
import greenmoonsoftware.es.command.AggregateCommandApplier
import greenmoonsoftware.es.command.Command
import greenmoonsoftware.es.event.Aggregate
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreEventSubscriber
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreQuery

abstract class AbstractJdbcCommandService {
    protected final Bus<Event, EventSubscriber> eventBus
    protected final JdbcStoreEventSubscriber eventStore
    protected final JdbcStoreQuery<Aggregate> query

    AbstractJdbcCommandService(Bus<Event, EventSubscriber> b, JdbcStoreEventSubscriber eventStore, JdbcStoreQuery<Aggregate> q) {
        eventBus = b
        this.eventStore = eventStore
        query = q
    }

    final void execute(Command command) {
        def aggregate = query.retrieve(command.aggregateId)

        def newEvents = AggregateCommandApplier.apply(aggregate, command)
        newEvents.each {
            eventStore.onEvent(it)
        }

        newEvents.each {
            eventBus.post(it)
        }
    }
}