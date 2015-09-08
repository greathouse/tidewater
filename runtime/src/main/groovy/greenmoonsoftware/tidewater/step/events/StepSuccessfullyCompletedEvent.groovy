package greenmoonsoftware.tidewater.step.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDouble

import java.time.Duration

final class StepSuccessfullyCompletedEvent extends AbstractEvent {
    Step step
    Date endDate
    Duration duration

    StepSuccessfullyCompletedEvent(Step s, Date end, Duration d) {
        super(s.name, "success")
        step = new StepDouble(s)
        endDate = end
        duration = d
    }
}
