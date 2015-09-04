package greenmoonsoftware.tidewater.config.events

import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.config.ContextAttributes

class ContextExecutionEndedEvent extends AbstractEvent {
    final private ContextAttributes attributes

    ContextExecutionEndedEvent(ContextAttributes a) {
        super(a.id, 'executionEnded')
        attributes = a
    }
}
