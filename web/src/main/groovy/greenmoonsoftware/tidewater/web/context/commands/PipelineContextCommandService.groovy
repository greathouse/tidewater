package greenmoonsoftware.tidewater.web.context.commands
import greenmoonsoftware.es.Bus
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreEventSubscriber
import greenmoonsoftware.tidewater.json.JsonEventSerializer
import greenmoonsoftware.tidewater.web.AbstractJdbcCommandService
import greenmoonsoftware.tidewater.web.context.PipelineContextEventStoreConfiguration

class PipelineContextCommandService extends AbstractJdbcCommandService {
    PipelineContextCommandService(Bus<Event, EventSubscriber> b, PipelineContextEventStoreConfiguration c) {
        super(b, new JdbcStoreEventSubscriber(c.toConfiguration(), c.datasource, new JsonEventSerializer()), new PipelineContextQuery(c.toConfiguration(), c.datasource))
    }
}
