package greenmoonsoftware.tidewater.config

import greenmoonsoftware.es.event.AbstractEvent

final class ContextExecutionStartedEvent extends AbstractEvent {
    final ContextAttributes attributes

    ContextExecutionStartedEvent(ContextAttributes a) {
        super(a.toString(), 'executionStarted')
        attributes = a
    }
}
