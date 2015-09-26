package greenmoonsoftware.tidewater.web.context.commands
import greenmoonsoftware.es.command.Command
import greenmoonsoftware.tidewater.config.ContextId

import java.time.Instant

class EndPipelineRunCommand implements Command {
    ContextId contextId
    Instant endTime

    EndPipelineRunCommand(ContextId contextId, Instant endTime) {
        this.contextId = contextId
        this.endTime = endTime
    }

    @Override
    String getAggregateId() {
        contextId.id
    }

    Instant getEndTime() {
        return endTime
    }
}
