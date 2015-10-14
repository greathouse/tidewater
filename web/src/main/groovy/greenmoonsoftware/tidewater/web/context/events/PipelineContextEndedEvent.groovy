package greenmoonsoftware.tidewater.web.context.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.context.ContextId
import greenmoonsoftware.tidewater.web.context.PipelineContextStatus

import java.time.Instant

class PipelineContextEndedEvent extends AbstractEvent {
    private Instant endTime
    private PipelineContextStatus status
    private String pipelineName

    protected PipelineContextEndedEvent(){}

    PipelineContextEndedEvent(ContextId c, String pipelineName, Instant endTime, PipelineContextStatus s) {
        super(c.id, PipelineContextEndedEvent.canonicalName)
        this.pipelineName = pipelineName
        this.endTime = endTime
        status = s
    }

    Instant getEndTime() {
        endTime
    }

    PipelineContextStatus getStatus() {
        status
    }

    String getPipelineName() {
        pipelineName
    }
}
