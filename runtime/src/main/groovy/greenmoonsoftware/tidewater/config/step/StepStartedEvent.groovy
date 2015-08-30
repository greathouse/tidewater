package greenmoonsoftware.tidewater.config.step
import greenmoonsoftware.es.event.AbstractEvent

class StepStartedEvent extends AbstractEvent {
    final Step step

    StepStartedEvent(Step step) {
        super(step.name, "started")
        this.step = step
    }
}
