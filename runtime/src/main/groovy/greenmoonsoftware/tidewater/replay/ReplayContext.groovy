package greenmoonsoftware.tidewater.replay

import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.es.event.SimpleEventBus
import greenmoonsoftware.es.event.jdbcstore.JdbcEventQuery
import greenmoonsoftware.tidewater.config.ContextAttributes
import greenmoonsoftware.tidewater.config.ContextId
import greenmoonsoftware.tidewater.json.JsonEventSerializer
import greenmoonsoftware.tidewater.config.TidewaterEventStoreConfiguration

public class ReplayContext {
    private final ContextAttributes attributes
    private final JdbcEventQuery eventQuery
    private final eventBus = new SimpleEventBus()

    ReplayContext(ContextId id) {
        attributes = new ContextAttributes(id)
        def storage = new TidewaterEventStoreConfiguration(attributes.metaDirectory)
        eventQuery = new JdbcEventQuery(storage.toConfiguration(), storage.datasource, new JsonEventSerializer())
    }

    void replay() {
        eventQuery.events.each { event ->
            eventBus.post(event)
        }
    }

    void addEventSubscribers(EventSubscriber<Event>... subscriber) {
        subscriber.each {eventBus.register(it)}
    }
}
