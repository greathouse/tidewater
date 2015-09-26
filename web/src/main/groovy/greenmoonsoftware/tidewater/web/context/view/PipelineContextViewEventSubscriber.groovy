package greenmoonsoftware.tidewater.web.context.view

import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.web.context.events.PipelineContextStartedEvent
import groovy.sql.Sql

import javax.sql.DataSource
import java.sql.Timestamp

class PipelineContextViewEventSubscriber implements EventSubscriber<Event> {
    private final DataSource dataSource
    private final Sql sql

    PipelineContextViewEventSubscriber(DataSource ds) {
        dataSource = ds
        sql = new Sql(ds)
    }

    @Override
    void onEvent(Event event) {
        EventApplier.apply(this, event)
    }

    private void handle(PipelineContextStartedEvent event) {
        sql.execute("""insert into PipelineRun
                (pipelineName, contextId, script, startTime, endTime)
                values (${event.pipelineName}, ${event.contextId.id}, ${event.script}, ${Timestamp.from(event.start)}, null)"""
        )
    }
}
