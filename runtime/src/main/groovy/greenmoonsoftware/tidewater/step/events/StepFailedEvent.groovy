package greenmoonsoftware.tidewater.step.events

import greenmoonsoftware.es.event.AbstractEvent
import greenmoonsoftware.tidewater.step.Step

import java.time.Duration

class StepFailedEvent extends AbstractEvent {
    Step step
    Date endDate
    Duration duration

    StepFailedEvent(Step s, Date end, Duration d) {
        super(s.name, 'step.failed')
        step = s
        endDate = end
        duration = d
    }
}
