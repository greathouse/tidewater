package greenmoonsoftware.tidewater.step.events

import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.step.StepConfiguration

class StepDefinedEvent extends AbstractEvent {
    StepConfiguration definition

    StepDefinedEvent(StepConfiguration d) {
        super(d.name, 'configured')
        definition = d
    }
}
