package greenmoonsoftware.tidewater.web.context.events

import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.config.ContextId

class PipelineContextAbortedEvent extends AbstractEvent {
    protected PipelineContextAbortedEvent(){}

    PipelineContextAbortedEvent(ContextId c) {
        super(c.id, PipelineContextAbortedEvent.canonicalName)
    }
}
