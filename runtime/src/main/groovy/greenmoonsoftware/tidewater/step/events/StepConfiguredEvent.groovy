package greenmoonsoftware.tidewater.step.events

import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.step.StepConfiguration

class StepConfiguredEvent extends AbstractEvent {
    StepConfiguration definition

    StepConfiguredEvent(StepConfiguration d) {
        super(d.name, 'configured')
        definition = d
    }
}
