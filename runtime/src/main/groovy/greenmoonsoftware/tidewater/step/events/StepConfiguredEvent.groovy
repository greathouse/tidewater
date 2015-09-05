package greenmoonsoftware.tidewater.step.events

import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.step.Step

class StepConfiguredEvent extends AbstractEvent {
    Step step

    StepConfiguredEvent(Step s) {
        super(s.name, 'step.configured')
        step = s
    }
}
