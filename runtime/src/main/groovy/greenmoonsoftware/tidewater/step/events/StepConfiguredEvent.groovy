package greenmoonsoftware.tidewater.step.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDouble

class StepConfiguredEvent extends AbstractEvent {
    Step step

    private StepConfiguredEvent(){}

    StepConfiguredEvent(Step s) {
        super(s.name, StepConfiguredEvent.canonicalName)
        step = new StepDouble(s)
    }
}
