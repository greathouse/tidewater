package greenmoonsoftware.tidewater.config.events

import greenmoonsoftware.tidewater.TidewaterEvent
import greenmoonsoftware.tidewater.config.ContextAttributes

final class ContextExecutionStartedEvent extends TidewaterEvent {
    final ContextAttributes attributes

    ContextExecutionStartedEvent(ContextAttributes a) {
        super(a.id.toString())
        attributes = a
    }
}
