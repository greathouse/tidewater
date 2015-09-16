package greenmoonsoftware.tidewater.web.pipeline.commands
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineStartedEvent

import java.time.Instant

class StartPipelineCommand {
    private final String name
    private final Instant start

    StartPipelineCommand(String name, Instant start) {
        this.name = name
        this.start = start
    }

    List<Event> execute() {
        [new PipelineStartedEvent(name, start)]
    }

    String getName() { name }
    Instant getStart() { start }
}
