package greenmoonsoftware.tidewater.web.context.events

import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.config.ContextId

import java.time.Instant

class PipelineContextStartedEvent extends AbstractEvent {
    String pipelineName
    ContextId contextId
    String script
    Instant start

    protected PipelineContextStartedEvent(){}

    PipelineContextStartedEvent(String pipelineName, ContextId contextId, String script, Instant start) {
        super(contextId.id, PipelineContextStartedEvent.canonicalName)
        this.pipelineName = pipelineName
        this.contextId = contextId
        this.script = script
        this.start = start
    }
}
