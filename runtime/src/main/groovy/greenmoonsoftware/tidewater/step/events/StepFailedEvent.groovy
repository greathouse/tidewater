package greenmoonsoftware.tidewater.step.events

import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.step.Step

import java.time.Duration

class StepFailedEvent extends AbstractEvent {
    Step step
    Date endDate
    Duration duration

    StepFailedEvent(Step s, Date endDate, Duration d) {
        super(s.name, 'step.failed')
        step = s
        this.endDate = endDate
        duration = d
    }
}
