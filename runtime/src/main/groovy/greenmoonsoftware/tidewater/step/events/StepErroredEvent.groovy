package greenmoonsoftware.tidewater.step.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.step.Step

import java.time.Duration

class StepErroredEvent extends AbstractEvent {
    Step step
    Date endDate
    Duration duration
    Exception exception

    StepErroredEvent(Step s, Date endDate, Duration d, Exception e) {
        super(step.name, 'step.errored')
        step = s
        this.endDate = endDate
        duration = d
        exception = e
    }
}
