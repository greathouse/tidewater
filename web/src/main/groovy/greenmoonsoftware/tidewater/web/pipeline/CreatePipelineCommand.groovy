package greenmoonsoftware.tidewater.web.pipeline
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineCreatedEvent
import groovy.transform.Immutable

@Immutable
class CreatePipelineCommand {
    String name
    String scriptText

    List<Event> execute() {
        if (!name) { throw new IllegalArgumentException('Parameter "name" is required.')}
        [new PipelineCreatedEvent(name, scriptText)]
    }
}
