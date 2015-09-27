package greenmoonsoftware.tidewater.web.context.commands
import greenmoonsoftware.es.event.Aggregate
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventList
import greenmoonsoftware.tidewater.config.ContextId
import greenmoonsoftware.tidewater.web.context.PipelineContextStatus
import greenmoonsoftware.tidewater.web.context.events.PipelineContextAbortedEvent
import greenmoonsoftware.tidewater.web.context.events.PipelineContextEndedEvent
import greenmoonsoftware.tidewater.web.context.events.PipelineContextErrorredEvent
import greenmoonsoftware.tidewater.web.context.events.PipelineContextFailedEvent
import greenmoonsoftware.tidewater.web.context.events.PipelineContextStartedEvent
import greenmoonsoftware.tidewater.web.context.events.PipelinePausedEvent

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
        [new PipelineContextErrorredEvent(new ContextId(command.aggregateId))]
    }

    private Collection<Event> handle(FailPipelineContextCommand c) {
        [new PipelineContextFailedEvent(new ContextId(c.aggregateId))]
    }

    private Collection<Event> handle(AbortPipelineContextCommand c) {
        [new PipelineContextAbortedEvent(new ContextId(c.aggregateId))]
    }

    private Collection<Event> handle(PausePipelineContextCommand c) {
        [new PipelinePausedEvent(new ContextId(c.aggregateId))]
    }

    @Override
    void apply(EventList events) {
        events.forEach { event -> EventApplier.apply(this, event) }
    }

    private void handle(PipelineContextErrorredEvent e) {
        status = PipelineContextStatus.ERROR
    }

    private void handle(PipelineContextFailedEvent e) {
        status = PipelineContextStatus.FAILURE
    }

    private void handle(PipelineContextAbortedEvent e) {
        status = PipelineContextStatus.ABORT
    }

    private void handle(PipelinePausedEvent e) {
        status = PipelineContextStatus.PAUSE
    }
}
