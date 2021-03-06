package greenmoonsoftware.tidewater.context
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.context.events.ContextExecutionStartedEvent
import greenmoonsoftware.tidewater.step.events.StepSuccessfullyCompletedEvent

class ContextAttributeEventSubscriber implements EventSubscriber<Event> {
    private final Context context
    private final ContextAttributes attributes

    ContextAttributeEventSubscriber(Context c, ContextAttributes a) {
        context = c
        attributes = a
    }

    @Override
    void onEvent(Event event) {
        EventApplier.apply(this, event)
    }

    private void handle(ContextExecutionStartedEvent event) {
        if (!attributes.script) {
            attributes.script = event.script
        }
    }

    private void handle(StepSuccessfullyCompletedEvent event) {
        context.addExecutedStep(event.step)
    }
}
