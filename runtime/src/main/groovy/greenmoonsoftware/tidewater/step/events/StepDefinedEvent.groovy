package greenmoonsoftware.tidewater.step.events

import greenmoonsoftware.tidewater.TidewaterEvent
import greenmoonsoftware.tidewater.step.StepConfiguration

class StepDefinedEvent extends TidewaterEvent {
    StepConfiguration definition

    StepDefinedEvent(StepConfiguration d) {
        super(d.name)
        definition = d
    }
}
