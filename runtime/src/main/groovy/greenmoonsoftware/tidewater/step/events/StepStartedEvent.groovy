package greenmoonsoftware.tidewater.step.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.step.Step

class StepStartedEvent extends AbstractEvent {
    final Step step
    final Date startTime

    StepStartedEvent(Step step, Date start) {
        super(step.name, "started")
        this.step = step
        startTime = start
    }
}
