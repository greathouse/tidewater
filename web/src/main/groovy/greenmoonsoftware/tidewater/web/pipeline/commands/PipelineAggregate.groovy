package greenmoonsoftware.tidewater.web.pipeline.commands
import greenmoonsoftware.es.event.Aggregate
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventList
import greenmoonsoftware.tidewater.config.ContextId
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineCreatedEvent
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineStartedEvent

import java.time.Instant

class PipelineAggregate implements Aggregate {
    private String name
    private String script
    private Instant lastStartTime

    @Override
    String getId() { name }

    String getScript() { script }

    Instant getLastStartTime() { lastStartTime }

    private Collection<Event> handle(CreatePipelineCommand command) {
        if (!command.name) { throw new IllegalArgumentException('Parameter "name" is required.')}
        if (name) {
            throw new RuntimeException("Pipeline with name \"${name}\" is already created")
        }
        [new PipelineCreatedEvent(command.name, command.scriptText)]
    }

    private Collection<Event> handle(StartPipelineCommand command) {
        [new PipelineStartedEvent(name, new ContextId("${name}-${new Date().format('yyyy-MM-dd_HH-mm-ss')}"), script, Instant.now())]
    }

    @Override
    void apply(EventList events) {
        events.forEach { event -> EventApplier.apply(this, event) }
    }

    private void handle(PipelineCreatedEvent event) {
        name = event.aggregateId
        script = event.scriptText
    }

    private void handle(PipelineStartedEvent event) {
        lastStartTime = event.start
    }
}
