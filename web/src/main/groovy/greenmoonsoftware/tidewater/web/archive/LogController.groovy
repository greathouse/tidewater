package greenmoonsoftware.tidewater.web.archive
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.config.ContextId
import greenmoonsoftware.tidewater.config.events.ContextExecutionStartedEvent
import greenmoonsoftware.tidewater.replay.ReplayContext
import greenmoonsoftware.tidewater.step.events.*
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class LogController {
    @RequestMapping('/archive/{contextId}')
    String index(Map<String, Object> model, @PathVariable('contextId') String contextId) {
        def replay = new ReplayContext(new ContextId(contextId))
        def steps = [:] as LinkedHashMap<String, ArchiveStep>
        String scriptText = ''
        replay.addEventSubscribers(new EventSubscriber<Event>() {
            @Override
            void onEvent(Event event) {
                EventApplier.apply(this, event)
            }

            private void handle(ContextExecutionStartedEvent event) {
                scriptText = event.script
            }

            private void handle(StepDefinedEvent event) {
                steps[event.definition.name] = new ArchiveStep(event.definition.name, event.definition.type.simpleName)
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
        model.put('script', scriptText)
        model.put('steps', new ArrayList<ArchiveStep>(steps.values()))
        return 'archive/index'
    }
}
