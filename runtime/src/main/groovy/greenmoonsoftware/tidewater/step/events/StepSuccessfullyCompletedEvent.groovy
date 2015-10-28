package greenmoonsoftware.tidewater.step.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.context.ContextId
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDouble

import java.time.Duration

final class StepSuccessfullyCompletedEvent extends AbstractEvent {
    Step step
    ContextId contextId
    Date endDate
    Duration duration

    StepSuccessfullyCompletedEvent(Step s, ContextId c, Date end, Duration d) {
        super(s.name, StepSuccessfullyCompletedEvent.canonicalName)
        step = new StepDouble(s)
        contextId = c
        endDate = end
        duration = d
    }

    StepSuccessfullyCompletedEvent() {
    }
}
