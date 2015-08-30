package greenmoonsoftware.tidewater.config.step
import greenmoonsoftware.es.event.AbstractEvent
import groovy.time.TimeDuration

final class StepSuccessEvent extends AbstractEvent {
    Step step
    TimeDuration duration

    StepSuccessEvent(Step s, TimeDuration d) {
        super(s.name, "success")
        step = s
        duration = d
    }
}
