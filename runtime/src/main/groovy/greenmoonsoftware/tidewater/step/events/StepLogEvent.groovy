package greenmoonsoftware.tidewater.step.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDouble

final class StepLogEvent extends AbstractEvent {
    Step step
    String message

    StepLogEvent() {
    }

    StepLogEvent(Step s, String msg) {
        super(s.name, StepLogEvent.canonicalName)
        step = new StepDouble(s)
        message = msg
    }
}
