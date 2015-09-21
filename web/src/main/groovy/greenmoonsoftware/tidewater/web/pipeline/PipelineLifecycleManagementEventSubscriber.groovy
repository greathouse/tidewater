package greenmoonsoftware.tidewater.web.pipeline
import greenmoonsoftware.es.Bus
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.config.NewContext
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineStartedEvent
import greenmoonsoftware.tidewater.web.pipeline.runs.commands.PipelineRunCommandService
import greenmoonsoftware.tidewater.web.pipeline.runs.commands.StartPipelineRunCommand
import greenmoonsoftware.tidewater.web.pipeline.runs.events.PipelineRunStartedEvent

class PipelineLifecycleManagementEventSubscriber implements EventSubscriber<Event> {
    private final Bus<Event, EventSubscriber> eventBus
    private final PipelineRunCommandService pipelineRunService

    PipelineLifecycleManagementEventSubscriber(Bus<Event, EventSubscriber> b, PipelineRunCommandService s) {
        eventBus = b
        pipelineRunService = s
    }

    @Override
    void onEvent(Event event) {
        EventApplier.apply(this, event)
    }

    private void handle(PipelineStartedEvent event) {
        pipelineRunService.execute(new StartPipelineRunCommand(event.aggregateId, event.contextId, event.script, event.start))
    }

    private void handle(PipelineRunStartedEvent event) {
        def context = new NewContext(event.contextId)
        context.addEventSubscribers(this)
        context.execute(event.script)
    }

//    private void handle(ContextExecutionEndedEvent event) {
//        eventBus.post(event)
//    }
}
