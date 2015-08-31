package greenmoonsoftware.tidewater.step

import greenmoonsoftware.es.event.AbstractEvent

final class StepLogEvent extends AbstractEvent {
    Step step
    String message

    StepLogEvent(Step step, String msg) {
        super(step.name, 'log')
        message = msg
    }
}
