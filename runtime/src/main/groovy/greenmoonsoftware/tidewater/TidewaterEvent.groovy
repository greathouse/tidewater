package greenmoonsoftware.tidewater

import greenmoonsoftware.es.event.AbstractEvent

abstract class TidewaterEvent extends AbstractEvent {
    protected TidewaterEvent(String aggregateId) {
        super(aggregateId, getClass().canonicalName)
    }
}
