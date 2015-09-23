package greenmoonsoftware.tidewater.web.pipeline.runs.commands
import greenmoonsoftware.es.Bus
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreEventSubscriber
import greenmoonsoftware.tidewater.json.JsonEventSerializer
import greenmoonsoftware.tidewater.web.AbstractCommandService
import greenmoonsoftware.tidewater.web.pipeline.runs.PipelineRunEventStoreConfiguration

class PipelineRunCommandService extends AbstractCommandService {
    PipelineRunCommandService(Bus<Event, EventSubscriber> b, PipelineRunEventStoreConfiguration c) {
        super(b, new JdbcStoreEventSubscriber(c.toConfiguration(), c.datasource, new JsonEventSerializer()), new PipelineRunQuery(c.toConfiguration(), c.datasource))
    }
}
