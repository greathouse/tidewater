package greenmoonsoftware.tidewater.web.context.commands
import greenmoonsoftware.es.command.Command
import greenmoonsoftware.tidewater.config.ContextId

import java.time.Instant

class EndPipelineContextCommand implements Command {
    ContextId contextId
    Instant endTime


    EndPipelineContextCommand(ContextId contextId, Instant endTime) {
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
