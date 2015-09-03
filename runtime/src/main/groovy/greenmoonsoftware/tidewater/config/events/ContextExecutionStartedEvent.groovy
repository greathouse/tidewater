package greenmoonsoftware.tidewater.config.events

import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.config.ContextAttributes

final class ContextExecutionStartedEvent extends AbstractEvent {
    final String script
    final ContextAttributes attributes

    ContextExecutionStartedEvent(String script, ContextAttributes a) {
        super(a.id, 'executionStarted')
        this.script = script
        attributes = a
    }
}
