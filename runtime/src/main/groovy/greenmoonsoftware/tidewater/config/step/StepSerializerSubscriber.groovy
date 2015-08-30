package greenmoonsoftware.tidewater.config.step
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber

final class StepSerializerSubscriber implements EventSubscriber<Event> {
    private File stepDirectory

    StepSerializerSubscriber(File stepDir) {
        stepDirectory = stepDir
    }

    @Override
    void onEvent(Event event) {
        EventApplier.apply(this, event)
    }

    void handle(StepSuccessfullyCompletedEvent event) {
        event.step.seralize(new File(stepDirectory, 'step.json'))
    }
}
