package greenmoonsoftware.tidewater.web.pipeline

import greenmoonsoftware.es.event.Aggregate
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventList
import greenmoonsoftware.tidewater.web.pipeline.commands.CreatePipelineCommand
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineCreatedEvent

class PipelineAggregate implements Aggregate {
    private String id
    private String script

    @Override
    String getId() { id }

    private Collection<Event> handle(CreatePipelineCommand command) {
        if (!command.name) { throw new IllegalArgumentException('Parameter "name" is required.')}
        [new PipelineCreatedEvent(command.name, command.scriptText)]
    }

    @Override
    void apply(EventList events) {
        events.forEach { event -> EventApplier.apply(this, event) }
    }

    private void handle(PipelineCreatedEvent event) {
        id = event.aggregateId
        script = event.scriptText
    }

    String getScript() { script }
}
