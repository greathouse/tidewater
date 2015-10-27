package greenmoonsoftware.tidewater.restart.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.context.ContextId

class ContextExecutionRestartedEvent extends AbstractEvent {

    ContextExecutionRestartedEvent(ContextId id) {
        super(id.toString(), ContextExecutionRestartedEvent.canonicalName)
    }

    ContextExecutionRestartedEvent() {
    }
}
