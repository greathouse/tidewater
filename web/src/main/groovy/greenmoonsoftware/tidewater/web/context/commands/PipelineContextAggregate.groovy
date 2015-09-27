package greenmoonsoftware.tidewater.web.context.commands
import greenmoonsoftware.es.event.Aggregate
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventList
import greenmoonsoftware.tidewater.config.ContextId
import greenmoonsoftware.tidewater.web.context.PipelineContextStatus
import greenmoonsoftware.tidewater.web.context.events.PipelineContextEndedEvent
import greenmoonsoftware.tidewater.web.context.events.PipelineContextStartedEvent

class PipelineContextAggregate implements Aggregate {
    private ContextId id
    private PipelineContextStatus status

    @Override
    String getId() {
        id
    }

    private Collection<Event> handle(StartPipelineContextCommand command) {
        [new PipelineContextStartedEvent(command.pipelineName, command.contextId, command.script, command.start)]
    }

    private Collection<Event> handle(EndPipelineContextCommand command) {
        [new PipelineContextEndedEvent(new ContextId(command.aggregateId), command.endTime, status ?: PipelineContextStatus.COMPLETE)]
    }

    private Collection<Event> handle(ErrorPipelineContextCommand command) {
        status = PipelineContextStatus.ERROR
        Collections.emptyList()
    }

    @Override
    void apply(EventList events) {
        events.forEach { event -> EventApplier.apply(this, event) }
    }
}
