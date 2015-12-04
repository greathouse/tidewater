package greenmoonsoftware.tidewater.step.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.context.ContextId

class StepDisabledEvent extends AbstractEvent {
    final ContextId contextId
    final Date skippedTime

    StepDisabledEvent() {
    }

    StepDisabledEvent(String name, ContextId c, Date skipped) {
        super(name, StepDisabledEvent.canonicalName)
        contextId = c
        skippedTime = skipped
    }
}
