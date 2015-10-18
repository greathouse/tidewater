package greenmoonsoftware.tidewater.web.context.commands

import greenmoonsoftware.es.command.CommandNotAllowedException
import greenmoonsoftware.es.event.Aggregate
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventList
import greenmoonsoftware.tidewater.context.ContextId
import greenmoonsoftware.tidewater.web.context.PipelineContextStatus
import greenmoonsoftware.tidewater.web.context.events.*

class PipelineContextAggregate implements Aggregate {
    private ContextId id
    private String pipelineName
    private PipelineContextStatus status

    @Override
    String getId() {
        id
    }

    private Collection<Event> handle(StartPipelineContextCommand command) {
        [new PipelineContextStartedEvent(command.pipelineName, command.contextId, command.script, command.start)]
    }

    private Collection<Event> handle(EndPipelineContextCommand command) {
        [new PipelineContextEndedEvent(new ContextId(command.aggregateId), pipelineName, command.endTime, status ?: PipelineContextStatus.COMPLETE)]
    }

    private Collection<Event> handle(ErrorPipelineContextCommand command) {
        ensureNotComplete()
        [new PipelineContextErrorredEvent(new ContextId(command.aggregateId))]
    }

    private Collection<Event> handle(FailPipelineContextCommand c) {
        ensureNotComplete()
        [new PipelineContextFailedEvent(new ContextId(c.aggregateId))]
    }

    private Collection<Event> handle(AbortPipelineContextCommand c) {
        ensureNotComplete()
        [new PipelineContextAbortedEvent(new ContextId(c.aggregateId))]
    }

    private Collection<Event> handle(PausePipelineContextCommand c) {
        ensureNotComplete()
        [new PipelinePausedEvent(new ContextId(c.aggregateId))]
    }

    private void ensureNotComplete() {
        if (status == PipelineContextStatus.COMPLETE) {
            throw new CommandNotAllowedException('Unable to issue command since this PipelineContext is already COMPLETED')
        }
    }

    @Override
    void apply(EventList events) {
        events.forEach { event -> EventApplier.apply(this, event) }
    }

    private void handle(PipelineContextStartedEvent e) {
        id = e.contextId
        pipelineName = e.pipelineName
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

    private void handle(PipelineContextEndedEvent e) {
        status = e.status
    }
}
