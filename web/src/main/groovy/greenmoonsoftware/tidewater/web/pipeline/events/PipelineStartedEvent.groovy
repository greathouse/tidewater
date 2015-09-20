package greenmoonsoftware.tidewater.web.pipeline.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.config.ContextId

import java.time.Instant

class PipelineStartedEvent extends AbstractEvent {
    ContextId contextId
    String script
    Instant start

    protected PipelineStartedEvent(){}

    PipelineStartedEvent(String name, ContextId cId, String script, Instant start) {
        super(name, PipelineStartedEvent.canonicalName)
        contextId = cId
        this.script = script
        this.start = start
    }
}
