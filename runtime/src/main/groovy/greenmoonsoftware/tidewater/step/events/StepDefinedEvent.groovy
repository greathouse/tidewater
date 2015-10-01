package greenmoonsoftware.tidewater.step.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.config.ContextId
import greenmoonsoftware.tidewater.step.StepDefinition

class StepDefinedEvent extends AbstractEvent {
    String name
    Class stepType
    ContextId contextId

    private StepDefinedEvent(){}

    StepDefinedEvent(StepDefinition d, ContextId c) {
        super(d.name, StepDefinedEvent.canonicalName)
        name = d.name
        stepType = d.type
        contextId = c
    }
}
