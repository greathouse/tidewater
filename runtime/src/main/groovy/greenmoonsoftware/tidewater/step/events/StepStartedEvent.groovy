package greenmoonsoftware.tidewater.step.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.context.ContextId
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDouble

class StepStartedEvent extends AbstractEvent {
    final Step step
    final ContextId contextId
    final Date startTime

    StepStartedEvent() {
    }

    StepStartedEvent(Step s, ContextId c, Date start) {
        super(s.name, StepStartedEvent.canonicalName)
        this.step = new StepDouble(s)
        contextId = c
        startTime = start
    }
}
