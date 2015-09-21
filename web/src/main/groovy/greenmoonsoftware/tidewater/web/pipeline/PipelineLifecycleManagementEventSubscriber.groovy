package greenmoonsoftware.tidewater.web.pipeline
import greenmoonsoftware.es.Bus
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.config.NewContext
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineStartedEvent

class PipelineLifecycleManagementEventSubscriber implements EventSubscriber<Event> {
    private final Bus<Event, EventSubscriber> eventBus

    PipelineLifecycleManagementEventSubscriber(Bus<Event, EventSubscriber> b) {
        eventBus = b
    }

    @Override
    void onEvent(Event event) {
        EventApplier.apply(this, event)
    }

    private void handle(PipelineStartedEvent event) {
        def context = new NewContext(event.contextId)
//        context.addEventSubscribers(this)
        context.execute(event.script)
    }

//    private void handle(ContextExecutionEndedEvent event) {
//        eventBus.post(event)
//    }
}
