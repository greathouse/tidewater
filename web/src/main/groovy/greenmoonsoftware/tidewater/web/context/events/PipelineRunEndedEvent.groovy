package greenmoonsoftware.tidewater.web.context.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.config.ContextId

import java.time.Instant

class PipelineRunEndedEvent extends AbstractEvent {
    private Instant endTime
    protected PipelineRunEndedEvent(){}

    PipelineRunEndedEvent(ContextId c, Instant endTime) {
        super(c.id, PipelineRunEndedEvent.canonicalName)
        this.endTime = endTime
    }

    Instant getEndTime() {
        endTime
    }
}
