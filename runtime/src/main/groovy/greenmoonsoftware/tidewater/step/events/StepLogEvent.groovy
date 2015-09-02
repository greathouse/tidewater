package greenmoonsoftware.tidewater.step.events

import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.step.Step

final class StepLogEvent extends AbstractEvent {
    Step step
    String message

    StepLogEvent(Step s, String msg) {
        super(s.name, 'log')
        step = s
        message = msg
    }
}
