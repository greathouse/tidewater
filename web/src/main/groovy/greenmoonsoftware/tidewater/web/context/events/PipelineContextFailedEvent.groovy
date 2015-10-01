package greenmoonsoftware.tidewater.web.context.events

import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.context.ContextId

class PipelineContextFailedEvent extends AbstractEvent {
    protected PipelineContextFailedEvent(){}

    PipelineContextFailedEvent(ContextId c) {
        super(c.id, PipelineContextFailedEvent.canonicalName)
    }
}
