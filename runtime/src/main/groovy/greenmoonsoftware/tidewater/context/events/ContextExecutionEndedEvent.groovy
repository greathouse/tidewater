package greenmoonsoftware.tidewater.context.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.context.ContextAttributes

class ContextExecutionEndedEvent extends AbstractEvent {
    ContextExecutionEndedEvent(ContextAttributes a) {
        super(a.id.toString(), ContextExecutionEndedEvent.canonicalName)
    }

    ContextExecutionEndedEvent() {
    }
}
