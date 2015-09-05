package greenmoonsoftware.tidewater.web.archive

import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.config.ContextId
import greenmoonsoftware.tidewater.replay.ReplayContext
import greenmoonsoftware.tidewater.step.events.StepErroredEvent
import greenmoonsoftware.tidewater.step.events.StepFailedEvent
import greenmoonsoftware.tidewater.step.events.StepLogEvent
import greenmoonsoftware.tidewater.step.events.StepStartedEvent
import greenmoonsoftware.tidewater.step.events.StepSuccessfullyCompletedEvent
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class LogController {
    @RequestMapping('/archive')
    String index(Map<String, Object> model) {
        def replay = new ReplayContext(new ContextId('2015-09-04_21-04-31'))
        def steps = [:] as LinkedHashMap<String, ArchiveStep>
        replay.addEventSubscribers(new EventSubscriber<Event>() {
            @Override
            void onEvent(Event event) {
                EventApplier.apply(this, event)
            }

            private void handle(StepStartedEvent event) {
                steps[event.step.name] = new ArchiveStep(event.step)
            }

            private void handle(StepLogEvent event) {
                steps[event.step.name].addLog event.message
            }

            private void handle(StepSuccessfullyCompletedEvent event) {
                steps[event.step.name].success()
            }

            private void handle(StepFailedEvent event) {
                steps[event.step.name].failed()
            }

            private void handle(StepErroredEvent event) {
                steps[event.step.name].errored()
            }
        })
        replay.replay()
        model.put('steps', new ArrayList<ArchiveStep>(steps.values()))
        return 'archive/index'
    }
}
