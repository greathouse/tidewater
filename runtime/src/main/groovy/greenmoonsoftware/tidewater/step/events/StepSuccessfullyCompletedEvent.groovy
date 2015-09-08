package greenmoonsoftware.tidewater.step.events

import greenmoonsoftware.tidewater.TidewaterEvent
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDouble

import java.time.Duration

final class StepSuccessfullyCompletedEvent extends TidewaterEvent {
    Step step
    Date endDate
    Duration duration

    StepSuccessfullyCompletedEvent(Step s, Date end, Duration d) {
        super(s.name)
        step = new StepDouble(s)
        endDate = end
        duration = d
    }
}
