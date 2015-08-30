package greenmoonsoftware.tidewater.runtime

import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.config.step.StepLogEvent
import greenmoonsoftware.tidewater.config.step.StepStartedEvent
import greenmoonsoftware.tidewater.config.step.StepSuccessEvent

final class StdoutLoggingSubscriber implements EventSubscriber<Event> {

    @Override
    void onEvent(Event event) {
        EventApplier.apply(this, event);
    }

    private void handle(StepStartedEvent event) {
        def step = event.step
        println '~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~'
        println "${step.name} (${step.class.simpleName})"
        step.inputs.each { println "\t${it.key}: ${it.value}" }
        println '~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~'
    }

    private void handle(StepSuccessEvent event) {
        println "\n${event.step.name} completed. Took ${event.duration}"
        println "Outputs:"
        event.step.outputs.each { println "\t${it.key}: ${it.value}" }
        println ''
    }

    private void handle(StepLogEvent event) {
        println event.message
    }
}
