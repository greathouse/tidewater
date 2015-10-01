package greenmoonsoftware.tidewater.web.context.commands

import greenmoonsoftware.es.command.Command
import greenmoonsoftware.tidewater.context.ContextId

class AbortPipelineContextCommand implements Command {
    ContextId contextId

    AbortPipelineContextCommand(ContextId c) {
        this.contextId = c
    }

    @Override
    String getAggregateId() {
        contextId.id
    }
}
