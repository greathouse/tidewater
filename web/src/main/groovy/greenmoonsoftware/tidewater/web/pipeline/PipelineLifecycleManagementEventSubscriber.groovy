package greenmoonsoftware.tidewater.web.pipeline

import greenmoonsoftware.es.Bus
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.context.ContextId
import greenmoonsoftware.tidewater.context.events.ContextExecutionEndedEvent
import greenmoonsoftware.tidewater.run.RunContext
import greenmoonsoftware.tidewater.step.events.StepErroredEvent
import greenmoonsoftware.tidewater.step.events.StepFailedEvent
import greenmoonsoftware.tidewater.web.PipelineContextContainer
import greenmoonsoftware.tidewater.web.context.commands.EndPipelineContextCommand
import greenmoonsoftware.tidewater.web.context.commands.ErrorPipelineContextCommand
import greenmoonsoftware.tidewater.web.context.commands.FailPipelineContextCommand
import greenmoonsoftware.tidewater.web.context.commands.PipelineContextCommandService
import greenmoonsoftware.tidewater.web.context.commands.StartPipelineContextCommand
import greenmoonsoftware.tidewater.web.context.events.PipelineContextStartedEvent
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineStartedEvent

class PipelineLifecycleManagementEventSubscriber implements EventSubscriber<Event> {
    private final Bus<Event, EventSubscriber> eventBus
    private final PipelineContextCommandService pipelineContextService
    private final PipelineContextContainer pipelineContextContainer
    private final EventSubscriber<Event> contextEventSubscriber

    PipelineLifecycleManagementEventSubscriber(
            Bus<Event, EventSubscriber> b,
            PipelineContextCommandService s,
            PipelineContextContainer c,
            EventSubscriber<Event> contextEventSubscriber
    ) {
        eventBus = b
        pipelineContextService = s
        pipelineContextContainer = c
        this.contextEventSubscriber = contextEventSubscriber
    }

    @Override
    void onEvent(Event event) {
        EventApplier.apply(this, event)
    }

    private void handle(PipelineStartedEvent event) {
        pipelineContextService.execute(new StartPipelineContextCommand(event.aggregateId, event.contextId, event.script, event.start))
    }

    private void handle(PipelineContextStartedEvent event) {
        def context = new RunContext(event.contextId)
        context.addEventSubscribers(this, contextEventSubscriber)
        def thread = context.execute(event.script)
        pipelineContextContainer.add(event.pipelineName, event.contextId, thread)
    }

    private void handle(StepFailedEvent e) {
        pipelineContextService.execute(new FailPipelineContextCommand(e.contextId))
    }

    private void handle(StepErroredEvent e) {
        pipelineContextService.execute(new ErrorPipelineContextCommand(e.contextId))
    }

    private void handle(ContextExecutionEndedEvent event) {
        pipelineContextService.execute(new EndPipelineContextCommand(new ContextId(event.aggregateId), event.eventDateTime))
    }
}
