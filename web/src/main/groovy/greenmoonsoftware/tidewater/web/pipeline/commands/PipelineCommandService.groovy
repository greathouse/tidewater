package greenmoonsoftware.tidewater.web.pipeline.commands
import greenmoonsoftware.es.Bus
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreEventSubscriber
import greenmoonsoftware.tidewater.json.JsonEventSerializer
import greenmoonsoftware.es.event.jdbcstore.AbstractJdbcCommandService
import greenmoonsoftware.tidewater.web.pipeline.PipelineEventStoreConfiguration

class PipelineCommandService extends AbstractJdbcCommandService {
    PipelineCommandService(Bus<Event, EventSubscriber> b, PipelineEventStoreConfiguration c) {
        super(b,
                new JdbcStoreEventSubscriber(c.toConfiguration(), c.datasource, new JsonEventSerializer()),
                new PipelineQuery(c.toConfiguration(), c.datasource)
        )
    }
}
