package greenmoonsoftware.tidewater.runtime

import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.config.events.ContextExecutionStartedEvent
import greenmoonsoftware.tidewater.step.events.StepErroredEvent
import greenmoonsoftware.tidewater.step.events.StepFailedEvent
import greenmoonsoftware.tidewater.step.events.StepLogEvent
import greenmoonsoftware.tidewater.step.events.StepStartedEvent
import greenmoonsoftware.tidewater.step.events.StepSuccessfullyCompletedEvent

import java.util.concurrent.TimeUnit

final class StdoutLoggingSubscriber implements EventSubscriber<Event> {

    @Override
    void onEvent(Event event) {
        EventApplier.apply(this, event);
    }

    private void handle(ContextExecutionStartedEvent event) {
        def attributes = event.attributes
        println "Workspace: ${attributes.workspace}"
        println "Number of steps: ${attributes.definedSteps.size()}"
    }

    private void handle(StepStartedEvent event) {
        def step = event.step
        println '~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~'
        println "${step.name} (${step.class.simpleName})"
        println '~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~'
    }

    private void handle(StepSuccessfullyCompletedEvent event) {
        println "\n\u001B[32mSUCCESS:\u001B[0m ${event.step.name} completed. Took ${convert(event.duration.toMillis())}"
    }

    private void handle(StepLogEvent event) {
        println event.message
    }

    private void handle(StepFailedEvent event) {
        println "\u001B[31mFAILED:\u001B[0m ${event.step.name}"
    }

    private void handle(StepErroredEvent event) {
        println "\u001B[31mErrored:\u001B[0m ${event.step.name}. (${event.stackTrace})"
    }

    public String convert(long millis) {
        def hrs = (int) TimeUnit.MILLISECONDS.toHours(millis) % 24
        def min = (int) TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        def sec = (int) TimeUnit.MILLISECONDS.toSeconds(millis) % 60
        def ms = (int) millis % 1000

        def s = String.format('%2d.%03d seconds', sec, ms)
        if (min) { s = String.format('%02d minutes, ', min) + s }
        if (hrs) { s = String.format('%02d hours, ') + s }
        return s
    }
}
