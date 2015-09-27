package greenmoonsoftware.tidewater.web.pipeline

import greenmoonsoftware.es.Bus
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.config.ContextId
import greenmoonsoftware.tidewater.config.NewContext
import greenmoonsoftware.tidewater.config.events.ContextExecutionEndedEvent
import greenmoonsoftware.tidewater.web.context.commands.EndPipelineContextCommand
import greenmoonsoftware.tidewater.web.context.commands.PipelineContextCommandService
import greenmoonsoftware.tidewater.web.context.commands.StartPipelineContextCommand
import greenmoonsoftware.tidewater.web.context.events.PipelineContextStartedEvent
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineStartedEvent

class PipelineLifecycleManagementEventSubscriber implements EventSubscriber<Event> {
    private final Bus<Event, EventSubscriber> eventBus
    private final PipelineContextCommandService pipelineContextService

    PipelineLifecycleManagementEventSubscriber(Bus<Event, EventSubscriber> b, PipelineContextCommandService s) {
        eventBus = b
        pipelineContextService = s
    }

    @Override
    void onEvent(Event event) {
        EventApplier.apply(this, event)
    }

    private void handle(PipelineStartedEvent event) {
        pipelineContextService.execute(new StartPipelineContextCommand(event.aggregateId, event.contextId, event.script, event.start))
    }

    private void handle(PipelineContextStartedEvent event) {
        def context = new NewContext(event.contextId)
        context.addEventSubscribers(this)
        context.execute(event.script)
    }

    private void handle(ContextExecutionEndedEvent event) {
        pipelineContextService.execute(new EndPipelineContextCommand(new ContextId(event.aggregateId), event.eventDateTime))
    }
}
