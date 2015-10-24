package greenmoonsoftware.tidewater.web.context.view.details
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.context.events.ContextExecutionStartedEvent
import greenmoonsoftware.tidewater.step.events.StepDefinedEvent
import greenmoonsoftware.tidewater.step.events.StepErroredEvent
import greenmoonsoftware.tidewater.step.events.StepFailedEvent
import greenmoonsoftware.tidewater.step.events.StepLogEvent
import greenmoonsoftware.tidewater.step.events.StepStartedEvent
import greenmoonsoftware.tidewater.step.events.StepSuccessfullyCompletedEvent

class PipelineContextViewDetailsReplayEventSubscriber implements EventSubscriber<Event> {
    private final PipelineContextViewDetails details

    PipelineContextViewDetailsReplayEventSubscriber(PipelineContextViewDetails d) {
        details = d
    }

    @Override
    void onEvent(Event event) {
        EventApplier.apply(this, event)
    }

    private void handle(ContextExecutionStartedEvent event) {
        details.with {
            scriptText = event.script
            workspace = event.workspace
            metadataDirectory = event.metaDirectory.absolutePath
        }
    }

    private void handle(StepDefinedEvent event) {
        details.defineStep(event.name, event.stepType.simpleName)
    }

    private void handle(StepStartedEvent event) {
        details.stepStarted(event.step.name, event.startTime)
    }

    private void handle(StepLogEvent event) {
        details.log(event.step.name, Date.from(event.eventDateTime), event.message)
    }

    private void handle(StepSuccessfullyCompletedEvent event) {
        details.stepSuccess(event.step, event.endDate)
    }

    private void handle(StepFailedEvent event) {
        details.stepFailed(event.step, event.endDate)
    }

    private void handle(StepErroredEvent event) {
        details.stepErrored(event.step, Date.from(event.eventDateTime), event.stackTrace)
    }
}
