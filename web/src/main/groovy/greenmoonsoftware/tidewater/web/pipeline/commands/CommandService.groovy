package greenmoonsoftware.tidewater.web.pipeline.commands
import greenmoonsoftware.es.Bus
import greenmoonsoftware.es.command.AggregateCommandApplier
import greenmoonsoftware.es.command.Command
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreEventSubscriber
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreQuery
import greenmoonsoftware.tidewater.json.JsonEventSerializer
import greenmoonsoftware.tidewater.web.pipeline.PipelineEventStoreConfiguration

class CommandService {
    private final Bus<Event, EventSubscriber> eventBus
    private final JdbcStoreEventSubscriber eventStore
    private final JdbcStoreQuery<PipelineAggregate> query

    CommandService(Bus<Event, EventSubscriber> b, PipelineEventStoreConfiguration c) {
        eventBus = b
        eventStore = new JdbcStoreEventSubscriber(c.toConfiguration(), c.datasource, new JsonEventSerializer())
        query = new CommandQuery(c.toConfiguration(), c.datasource)
    }

    void execute(Command command) {
        def pipeline = query.retrieve(command.aggregateId)

        def newEvents = AggregateCommandApplier.apply(pipeline, command)
        newEvents.each {
            eventStore.onEvent(it)
        }

        newEvents.each {
            eventBus.post(it)
        }
    }
}
