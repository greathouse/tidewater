package greenmoonsoftware.tidewater.web.pipeline.view
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineCreatedEvent

class PipelineViewEventSubscriber implements EventSubscriber<Event> {
    private final PipelineViewRepository repository

    PipelineViewEventSubscriber(PipelineViewRepository r) {
        repository = r
    }

    @Override
    void onEvent(Event event) {
        EventApplier.apply(this, event)
    }

    private void handle(PipelineCreatedEvent event) {
        repository.save(new PipelineView([
                name: event.aggregateId,
                script: event.scriptText
        ]))
    }
}