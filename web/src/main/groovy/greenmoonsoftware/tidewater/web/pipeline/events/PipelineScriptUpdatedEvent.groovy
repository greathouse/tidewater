package greenmoonsoftware.tidewater.web.pipeline.events

import greenmoonsoftware.es.event.AbstractEvent

class PipelineScriptUpdatedEvent extends AbstractEvent {
    String script

    protected PipelineScriptUpdatedEvent(){}

    PipelineScriptUpdatedEvent(String name, String script) {
        super(name, PipelineScriptUpdatedEvent.canonicalName)
        this.script = script
    }
}
