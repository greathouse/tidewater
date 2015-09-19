package greenmoonsoftware.tidewater.web.pipeline.events

import greenmoonsoftware.es.event.AbstractEvent

class PipelineCreatedEvent extends AbstractEvent {
    final String scriptText

    protected PipelineCreatedEvent(){}

    PipelineCreatedEvent(String name, String script) {
        super(name, PipelineCreatedEvent.canonicalName)
        this.scriptText = script
    }
}
