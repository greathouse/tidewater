package greenmoonsoftware.tidewater.web.context.view
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.web.context.PipelineContextStatus
import greenmoonsoftware.tidewater.web.context.events.PipelineContextEndedEvent
import greenmoonsoftware.tidewater.web.context.events.PipelineContextStartedEvent
import groovy.sql.Sql

import javax.sql.DataSource
import java.sql.Timestamp

class PipelineContextViewEventSubscriber implements EventSubscriber<Event> {
    private final DataSource dataSource
    private final Sql sql
    private final PipelineContextViewRepository repository

    PipelineContextViewEventSubscriber(DataSource ds, PipelineContextViewRepository r) {
        dataSource = ds
        sql = new Sql(ds)
        repository = r
    }

    @Override
    void onEvent(Event event) {
        EventApplier.apply(this, event)
    }

    private void handle(PipelineContextStartedEvent event) {
        repository.save(new PipelineContextView(
                contextId: event.contextId,
                pipelineName: event.pipelineName,
                status: PipelineContextStatus.IN_PROGRESS,
                startTime: Date.from(event.start),
                endTime: new Date(0)
        ))
    }

    private void handle(PipelineContextEndedEvent event) {
        sql.execute("""
                update Pipeline_Context_View set
                    end_Time = ${Timestamp.from(event.endTime)},
                    status = ${PipelineContextStatus.COMPLETE.value}
                where context_Id = ${event.aggregateId}""")
    }
}
