package greenmoonsoftware.tidewater.step.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDouble

class StepStartedEvent extends AbstractEvent {
    final Step step
    final Date startTime

    StepStartedEvent(Step s, Date start) {
        super(s.name, "started")
        this.step = new StepDouble(s)
        startTime = start
    }
}
