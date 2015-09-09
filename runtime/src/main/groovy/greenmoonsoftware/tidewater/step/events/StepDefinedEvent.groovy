package greenmoonsoftware.tidewater.step.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.step.StepDefinition

class StepDefinedEvent extends AbstractEvent {
    String name
    Class stepType

    private StepDefinedEvent(){}

    StepDefinedEvent(StepDefinition d) {
        super(d.name, StepDefinedEvent.canonicalName)
        name = d.name
        stepType = d.type
    }
}
