package greenmoonsoftware.tidewater.replay
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.es.event.SimpleEventBus
import greenmoonsoftware.es.event.jdbcstore.JdbcEventQuery
import greenmoonsoftware.tidewater.plugins.PluginClassLoader
import greenmoonsoftware.tidewater.context.ContextAttributes
import greenmoonsoftware.tidewater.context.ContextId
import greenmoonsoftware.tidewater.context.TidewaterEventStoreConfiguration
import greenmoonsoftware.tidewater.json.JsonEventSerializer

public class ReplayRunner {
    private final ContextAttributes attributes
    private final JdbcEventQuery eventQuery
    private final eventBus = new SimpleEventBus()

    ReplayRunner(ContextId id) {
        Thread.currentThread().setContextClassLoader(classLoader())
        attributes = new ContextAttributes(id)
        def storage = new TidewaterEventStoreConfiguration(attributes.metaDirectory)
        eventQuery = new JdbcEventQuery(storage.toConfiguration(), storage.datasource, new JsonEventSerializer())
    }

    private ClassLoader classLoader() {
        return new PluginClassLoader()
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
