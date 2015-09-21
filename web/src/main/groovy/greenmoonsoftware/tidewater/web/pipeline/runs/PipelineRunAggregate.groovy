package greenmoonsoftware.tidewater.web.pipeline.runs

import greenmoonsoftware.es.event.Aggregate
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventList
import greenmoonsoftware.tidewater.config.ContextId
import greenmoonsoftware.tidewater.web.pipeline.runs.commands.StartPipelineRunCommand
import greenmoonsoftware.tidewater.web.pipeline.runs.events.PipelineRunStartedEvent

class PipelineRunAggregate implements Aggregate {
    private ContextId id

    @Override
    String getId() {
        id
    }

    private Collection<Event> handle(StartPipelineRunCommand command) {
        [new PipelineRunStartedEvent(command.pipelineName, command.contextId, command.script, command.start)]
    }

    @Override
    void apply(EventList events) {
        events.forEach { event -> EventApplier.apply(this, event) }
    }
}
