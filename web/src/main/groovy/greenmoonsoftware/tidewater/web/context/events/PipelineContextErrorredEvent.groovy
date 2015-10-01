package greenmoonsoftware.tidewater.web.context.events

import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.context.ContextId

class PipelineContextErrorredEvent extends AbstractEvent {
    protected PipelineContextErrorredEvent(){}

    PipelineContextErrorredEvent(ContextId contextId) {
        super(contextId.id, PipelineContextErrorredEvent.canonicalName)
    }
}
