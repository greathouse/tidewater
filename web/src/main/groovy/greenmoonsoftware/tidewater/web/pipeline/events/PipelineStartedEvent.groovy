package greenmoonsoftware.tidewater.web.pipeline.events

import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.config.ContextId

import java.time.Instant

class PipelineStartedEvent extends AbstractEvent {
    ContextId contextId
    Instant start

    PipelineStartedEvent(String name, ContextId cId, Instant start) {
        super(name, PipelineStartedEvent.canonicalName)
        this.contextId = cId
        this.start = start
    }
}
