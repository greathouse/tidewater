package greenmoonsoftware.tidewater.config

import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.step.events.StepConfiguredEvent
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

    private void handle(StepSuccessfullyCompletedEvent event) {
        attributes.addExecutedStep(event.step)
    }

    private void handle(StepConfiguredEvent event) {
        attributes.addDefinedStep(event.definition)
    }
}
