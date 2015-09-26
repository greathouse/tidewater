package greenmoonsoftware.tidewater.web.context.events

import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.config.ContextId

import java.time.Instant

class PipelineRunStartedEvent extends AbstractEvent {
    String pipelineName
    ContextId contextId
    String script
    Instant start

    protected PipelineRunStartedEvent(){}

    PipelineRunStartedEvent(String pipelineName, ContextId contextId, String script, Instant start) {
        super(contextId.id, PipelineRunStartedEvent.canonicalName)
        this.pipelineName = pipelineName
        this.contextId = contextId
        this.script = script
        this.start = start
    }
}
