package greenmoonsoftware.tidewater.config.events

import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.config.ContextAttributes

final class ContextExecutionStartedEvent extends AbstractEvent {
    final ContextAttributes attributes

    ContextExecutionStartedEvent(ContextAttributes a) {
        super(a.id, 'executionStarted')
        attributes = a
    }
}
