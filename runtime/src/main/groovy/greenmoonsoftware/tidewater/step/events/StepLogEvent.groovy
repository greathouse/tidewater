package greenmoonsoftware.tidewater.step.events

import greenmoonsoftware.tidewater.TidewaterEvent
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDouble

final class StepLogEvent extends TidewaterEvent {
    Step step
    String message

    StepLogEvent(Step s, String msg) {
        super(s.name)
        step = new StepDouble(s)
        message = msg
    }
}
