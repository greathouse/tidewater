package greenmoonsoftware.tidewater.web.pipeline.events

import greenmoonsoftware.es.event.AbstractEvent

class PipelineCreatedEvent extends AbstractEvent {
    final String name
    final String scriptText

    PipelineCreatedEvent(String name, String script) {
        this.name = name
        this.scriptText = script
    }
}
