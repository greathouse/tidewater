package greenmoonsoftware.tidewater.web.pipeline.commands
import greenmoonsoftware.es.command.Command

import java.time.Instant

class EndPipelineCommand implements Command {
    String name
    Instant endTime

    @Override
    String getAggregateId() {
        name
    }
}
