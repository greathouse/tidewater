package greenmoonsoftware.tidewater.context.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.context.ContextAttributes

final class ContextExecutionStartedEvent extends AbstractEvent {
    final String script
    final File workspace
    final File metaDirectory

    private ContextExecutionStartedEvent(){}

    ContextExecutionStartedEvent(ContextAttributes a) {
        super(a.id.toString(), ContextExecutionStartedEvent.canonicalName)
        script = a.script
        workspace = a.workspace
        metaDirectory = a.metaDirectory
    }
}
