package greenmoonsoftware.tidewater.web.context.events

import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.context.ContextId

class PipelinePausedEvent extends AbstractEvent {
    protected PipelinePausedEvent(){}

    PipelinePausedEvent(ContextId c) {
        super(c.id, PipelinePausedEvent.canonicalName)
    }
}
