package greenmoonsoftware.tidewater.config.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.config.ContextAttributes

class ContextExecutionEndedEvent extends AbstractEvent {
    ContextExecutionEndedEvent(ContextAttributes a) {
        super(a.id.toString(), ContextExecutionEndedEvent.canonicalName)
    }

    ContextExecutionEndedEvent() {
    }
}
