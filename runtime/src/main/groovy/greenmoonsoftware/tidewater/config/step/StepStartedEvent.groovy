package greenmoonsoftware.tidewater.config.step
import greenmoonsoftware.es.event.AbstractEvent

class StepStartedEvent extends AbstractEvent {
    final Step step
    final Date startTime

    StepStartedEvent(Step step, Date start) {
        super(step.name, "started")
        this.step = step
        startTime = start
    }
}
