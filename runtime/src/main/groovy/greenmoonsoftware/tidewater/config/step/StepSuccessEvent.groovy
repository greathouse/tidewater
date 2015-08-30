package greenmoonsoftware.tidewater.config.step
import greenmoonsoftware.es.event.AbstractEvent
import groovy.time.TimeDuration

final class StepSuccessEvent extends AbstractEvent {
    Step step
    Date endTime
    TimeDuration duration

    StepSuccessEvent(Step s, Date end, TimeDuration d) {
        super(s.name, "success")
        step = s
        endTime = end
        duration = d
    }
}
