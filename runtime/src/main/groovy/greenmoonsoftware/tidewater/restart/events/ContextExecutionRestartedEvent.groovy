package greenmoonsoftware.tidewater.restart.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.config.ContextId

class ContextExecutionRestartedEvent extends AbstractEvent {
    final ContextId id

    ContextExecutionRestartedEvent(ContextId id) {
        super(id.toString(), ContextExecutionRestartedEvent.canonicalName)
        this.id = id
    }

    ContextExecutionRestartedEvent() {
    }
}
