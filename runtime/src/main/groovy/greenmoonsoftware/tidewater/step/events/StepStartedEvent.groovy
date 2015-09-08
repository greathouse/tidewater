package greenmoonsoftware.tidewater.step.events

import greenmoonsoftware.tidewater.TidewaterEvent
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDouble

class StepStartedEvent extends TidewaterEvent {
    final Step step
    final Date startTime

    StepStartedEvent(Step s, Date start) {
        super(s.name)
        this.step = new StepDouble(s)
        startTime = start
    }
}
