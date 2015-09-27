package greenmoonsoftware.tidewater.web.context.view

import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.web.context.events.PipelineContextEndedEvent
import greenmoonsoftware.tidewater.web.context.events.PipelineContextStartedEvent
import groovy.sql.Sql

import javax.sql.DataSource
import java.sql.Timestamp
import java.time.Instant

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
        sql.execute("""
                insert into PipelineContext (
                    pipelineName,
                    contextId,
                    status,
                    startTime,
                    endTime
                ) values (
                    ${event.pipelineName},
                    ${event.contextId.id},
                    ${PipelineContextView.Status.IN_PROGRESS.value},
                    ${Timestamp.from(event.start)},
                    ${Timestamp.from(Instant.EPOCH)}
                )"""
        )
    }

    private void handle(PipelineContextEndedEvent event) {
        sql.execute("""
                update PipelineContext set
                    endTime = ${Timestamp.from(event.endTime)},
                    status = ${PipelineContextView.Status.COMPLETE.value}
                where contextId = ${event.aggregateId}""")
    }
}
