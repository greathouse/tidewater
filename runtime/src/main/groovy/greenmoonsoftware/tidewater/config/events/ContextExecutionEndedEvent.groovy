package greenmoonsoftware.tidewater.config.events

import greenmoonsoftware.tidewater.TidewaterEvent
import greenmoonsoftware.tidewater.config.ContextAttributes

class ContextExecutionEndedEvent extends TidewaterEvent {
    final private ContextAttributes attributes

    ContextExecutionEndedEvent(ContextAttributes a) {
        super(a.id.toString())
        attributes = a
    }
}
