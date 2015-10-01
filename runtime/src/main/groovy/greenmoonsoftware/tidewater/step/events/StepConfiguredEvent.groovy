package greenmoonsoftware.tidewater.step.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.config.ContextId
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDouble

class StepConfiguredEvent extends AbstractEvent {
    Step step
    ContextId contextId

    private StepConfiguredEvent(){}

    StepConfiguredEvent(Step s, ContextId c) {
        super(s.name, StepConfiguredEvent.canonicalName)
        step = new StepDouble(s)
        contextId = c
    }
}
