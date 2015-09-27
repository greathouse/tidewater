package greenmoonsoftware.tidewater.step.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.config.ContextId
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDouble

import java.time.Duration

class StepFailedEvent extends AbstractEvent {
    Step step
    ContextId contextId
    Date endDate
    Duration duration

    StepFailedEvent() {
    }

    StepFailedEvent(Step s, ContextId c, Date end, Duration d) {
        super(s.name, StepFailedEvent.canonicalName)
        step = new StepDouble(s)
        contextId = c
        endDate = end
        duration = d
    }
}
