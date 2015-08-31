package greenmoonsoftware.tidewater.step
import greenmoonsoftware.es.event.AbstractEvent

import java.time.Duration

final class StepSuccessfullyCompletedEvent extends AbstractEvent {
    Step step
    Date endDate
    Duration duration

    StepSuccessfullyCompletedEvent(Step s, Date end, Duration d) {
        super(s.name, "success")
        step = s
        endDate = end
        duration = d
    }
}
