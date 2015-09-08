package greenmoonsoftware.tidewater.step.events

import greenmoonsoftware.tidewater.TidewaterEvent
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDouble

import java.time.Duration

class StepErroredEvent extends TidewaterEvent {
    Step step
    Date endDate
    Duration duration
    String stackTrace

    StepErroredEvent(Step s, Date end, Duration d, Exception e) {
        super(s.name)
        step = new StepDouble(s)
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
