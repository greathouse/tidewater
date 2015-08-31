package greenmoonsoftware.tidewater.step

import greenmoonsoftware.es.event.AbstractEvent

class StepConfiguredEvent extends AbstractEvent {
    StepConfiguration definition

    StepConfiguredEvent(StepConfiguration d) {
        super(d.name, 'configured')
        definition = d
    }
}
