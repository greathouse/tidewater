package greenmoonsoftware.tidewater.step.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.step.Step

import java.time.Duration

class StepErroredEvent extends AbstractEvent {
    Step step
    Date endDate
    Duration duration
    String stackTrace

    StepErroredEvent(Step s, Date end, Duration d, Exception e) {
        super(s.name, 'step.failed')
        step = s
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
