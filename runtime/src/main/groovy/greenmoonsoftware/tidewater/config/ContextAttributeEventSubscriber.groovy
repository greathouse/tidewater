package greenmoonsoftware.tidewater.config

import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.config.events.ContextExecutionStartedEvent
import greenmoonsoftware.tidewater.step.events.StepDefinedEvent
import greenmoonsoftware.tidewater.step.events.StepSuccessfullyCompletedEvent

class ContextAttributeEventSubscriber implements EventSubscriber<Event> {
    private final ContextAttributes attributes

    ContextAttributeEventSubscriber(ContextAttributes a) {
        attributes = a
    }

    @Override
    void onEvent(Event event) {
        EventApplier.apply(this, event)
    }

    private void handle(ContextExecutionStartedEvent event) {
        if (!attributes.script) {
            attributes.script = event.attributes.script
        }
    }

    private void handle(StepSuccessfullyCompletedEvent event) {
        attributes.addExecutedStep(event.step)
    }

    private void handle(StepDefinedEvent event) {
        attributes.addDefinedStep(event.definition)
    }
}
