package greenmoonsoftware.tidewater.web.context.commands

import greenmoonsoftware.es.command.Command
import greenmoonsoftware.tidewater.context.ContextId

class ErrorPipelineContextCommand implements Command {
    ContextId contextId

    ErrorPipelineContextCommand(ContextId c) {
        contextId = c
    }

    @Override
    String getAggregateId() {
        contextId.id
    }
}
