package greenmoonsoftware.tidewater.config

import greenmoonsoftware.es.event.AbstractEvent

final class ContextExecutionStartedEvent extends AbstractEvent {
    final Context context

    ContextExecutionStartedEvent(Context c) {
        super(c.toString(), 'executionStarted')
        context = c
    }
}
