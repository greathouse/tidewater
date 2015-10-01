package greenmoonsoftware.tidewater.web.context.commands

import greenmoonsoftware.es.command.Command
import greenmoonsoftware.tidewater.context.ContextId

class FailPipelineContextCommand implements Command {
    ContextId contextId

    FailPipelineContextCommand(ContextId c) {
        contextId = c
    }

    @Override
    String getAggregateId() {
        contextId.id
    }
}
