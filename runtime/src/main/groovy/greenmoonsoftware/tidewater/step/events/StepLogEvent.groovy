package greenmoonsoftware.tidewater.step.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.context.ContextId
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDouble

final class StepLogEvent extends AbstractEvent {
    Step step
    String logMessageId
    ContextId contextId
    String message

    StepLogEvent() {
    }

    StepLogEvent(Step s, ContextId c, String msg) {
        super(s.name, StepLogEvent.canonicalName)
        step = new StepDouble(s)
        contextId = c
        message = msg
    }

    String getLogMessageId() {
        return logMessageId ?: id.toString()
    }
}
