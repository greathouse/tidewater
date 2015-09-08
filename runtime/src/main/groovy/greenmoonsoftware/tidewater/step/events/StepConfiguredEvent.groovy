package greenmoonsoftware.tidewater.step.events

import greenmoonsoftware.tidewater.TidewaterEvent
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDouble

class StepConfiguredEvent extends TidewaterEvent {
    Step step

    StepConfiguredEvent(Step s) {
        super(s.name)
        step = new StepDouble(s)
    }
}
