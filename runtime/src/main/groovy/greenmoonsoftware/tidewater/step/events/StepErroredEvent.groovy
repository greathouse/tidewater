package greenmoonsoftware.tidewater.step.events
import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.step.Step

import java.time.Duration

class StepErroredEvent extends AbstractEvent {
    Step step
    Date endDate
    Duration duration
    Exception exception

    StepErroredEvent(Step s, Date end, Duration d, Exception e) {
        super(s.name, 'step.failed')
        step = s
        endDate = end
        duration = d
        exception = e
    }
}
