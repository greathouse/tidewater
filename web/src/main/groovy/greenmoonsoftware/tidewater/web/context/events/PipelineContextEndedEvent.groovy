package greenmoonsoftware.tidewater.web.context.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.config.ContextId

import java.time.Instant

class PipelineContextEndedEvent extends AbstractEvent {
    private Instant endTime
    protected PipelineContextEndedEvent(){}

    PipelineContextEndedEvent(ContextId c, Instant endTime) {
        super(c.id, PipelineContextEndedEvent.canonicalName)
        this.endTime = endTime
    }

    Instant getEndTime() {
        endTime
    }
}
