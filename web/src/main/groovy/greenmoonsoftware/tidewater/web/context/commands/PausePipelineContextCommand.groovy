package greenmoonsoftware.tidewater.web.context.commands

import greenmoonsoftware.es.command.Command
import greenmoonsoftware.tidewater.context.ContextId

class PausePipelineContextCommand implements Command {
    ContextId contextId

    PausePipelineContextCommand(ContextId c) {
        contextId = c
    }

    @Override
    String getAggregateId() {
        contextId.id
    }
}
