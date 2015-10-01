package greenmoonsoftware.tidewater.web.archive
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.context.ContextId
import greenmoonsoftware.tidewater.context.events.ContextExecutionStartedEvent
import greenmoonsoftware.tidewater.replay.ReplayRunner
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.events.*
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Controller
class LogController {
    @RequestMapping('/archive/{contextId}')
    String index(Map<String, Object> model, @PathVariable('contextId') String contextId) {
        def replay = new ReplayRunner(new ContextId(contextId))
        def steps = [:] as LinkedHashMap<String, ArchiveStep>
        String scriptText = ''
        File workspace
        File metaDirectory

        replay.addEventSubscribers(new EventSubscriber<Event>() {
            @Override
            void onEvent(Event event) {
                EventApplier.apply(this, event)
            }

            private void handle(ContextExecutionStartedEvent event) {
                scriptText = event.script
                workspace = event.workspace
                metaDirectory = event.metaDirectory
            }

            private void handle(StepDefinedEvent event) {
                if (!steps[event.name]) {
                    steps[event.name] = new ArchiveStep(event.name, event.stepType.simpleName)
                }
            }

            private void handle(StepStartedEvent event) {
                steps[event.step.name].newAttempt()
            }

            private void handle(StepLogEvent event) {
                def formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").withZone(ZoneId.systemDefault())
                steps[event.step.name].addLog "<strong>${formatter.format(event.eventDateTime)}:</strong> ${event.message}"
            }

            private void handle(StepSuccessfullyCompletedEvent event) {
                stepEnded(event.step).success()
            }

            private void handle(StepFailedEvent event) {
                stepEnded(event.step).failed()

            }

            private void handle(StepErroredEvent event) {
                def a = stepEnded(event.step)
                a.errored()
                a.addLog(event.stackTrace)

            }

            private ArchiveStep stepEnded(Step step) {
                def archiveStep = steps[step.name]
                def c = {
                    archiveStep.addAttribute(it.key, it.value.toString() ? it.value.toString().replaceAll('\n', '<br />') : '&nbsp;')
                }
                step.inputs.each c
                step.outputs.each c
                return archiveStep
            }
        })
        replay.replay()
        model.put('contextId', contextId)
        model.put('script', scriptText)
        model.put('attributes', [
                new Kv('id', contextId),
                new Kv('workspace', workspace.absolutePath),
                new Kv('metaDirectory', metaDirectory.absolutePath)
        ])
        model.put('steps', new ArrayList<ArchiveStep>(steps.values()))
        return 'archive/index'
    }
}
