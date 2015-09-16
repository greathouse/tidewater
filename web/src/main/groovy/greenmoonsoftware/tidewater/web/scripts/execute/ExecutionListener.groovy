package greenmoonsoftware.tidewater.web.scripts.execute
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.config.events.ContextExecutionStartedEvent
import org.springframework.stereotype.Component

@Component
class ExecutionListener implements EventSubscriber<Event> {
    private final Map<String, String> idToScriptName = [:]
    private final Map<String, String> currentlyExecuting = [:]

    void addMap(String contextId, String scriptName) {
        idToScriptName[contextId] = scriptName
    }

    @Override
    void onEvent(Event event) {
        EventApplier.apply(this, event)
    }

    void handle(ContextExecutionStartedEvent event) {
        currentlyExecuting[event.aggregateId] = idToScriptName[event.aggregateId]
    }
}
