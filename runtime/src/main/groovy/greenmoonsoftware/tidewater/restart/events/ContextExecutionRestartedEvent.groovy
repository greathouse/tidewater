package greenmoonsoftware.tidewater.restart.events

import greenmoonsoftware.tidewater.TidewaterEvent
import greenmoonsoftware.tidewater.config.ContextId

class ContextExecutionRestartedEvent extends TidewaterEvent {
    final ContextId id

    ContextExecutionRestartedEvent(ContextId id) {
        super(id.toString())
        this.id = id
    }
}
