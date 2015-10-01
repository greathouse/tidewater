package greenmoonsoftware.tidewater.step.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.config.ContextId
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDouble

import java.time.Duration

class StepErroredEvent extends AbstractEvent {
    Step step
    ContextId contextId
    Date endDate
    Duration duration
    String stackTrace

    StepErroredEvent() {
    }

    StepErroredEvent(Step s, ContextId c, Date end, Duration d, Exception e) {
        super(s.name, StepErroredEvent.canonicalName)
        step = new StepDouble(s)
        contextId = c
        endDate = end
        duration = d
        stackTrace = convertToString(e)
    }

    private String convertToString(Exception e) {
        def sw = new StringWriter()
        def pw = new PrintWriter(sw)
        e?.printStackTrace(pw)
        return sw.toString()
    }
}
