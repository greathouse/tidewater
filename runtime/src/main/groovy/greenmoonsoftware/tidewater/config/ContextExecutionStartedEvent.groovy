package greenmoonsoftware.tidewater.config

import greenmoonsoftware.es.event.AbstractEvent

final class ContextExecutionStartedEvent extends AbstractEvent {
    final ContextAttributes attributes

    ContextExecutionStartedEvent(ContextAttributes a) {
        super(a.id, 'executionStarted')
        attributes = a
    }
}
