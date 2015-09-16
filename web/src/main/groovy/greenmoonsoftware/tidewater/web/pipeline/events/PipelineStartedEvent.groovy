package greenmoonsoftware.tidewater.web.pipeline.events

import greenmoonsoftware.es.event.AbstractEvent

import java.time.Instant

class PipelineStartedEvent extends AbstractEvent {
    Instant start

    PipelineStartedEvent(String name, Instant start) {
        super(name, PipelineStartedEvent.canonicalName)
        this.start = start
    }
}
