package greenmoonsoftware.tidewater.web.context.commands
import greenmoonsoftware.es.command.Command
import greenmoonsoftware.tidewater.context.ContextId

import java.time.Instant

class StartPipelineContextCommand implements Command {
    String pipelineName
    ContextId contextId
    String script
    Instant start

    StartPipelineContextCommand(String pipelineName, ContextId id, String script, Instant start) {
        this.pipelineName = pipelineName
        contextId = id
        this.script = script
        this.start = start
    }

    @Override
    String getAggregateId() {
        contextId.toString()
    }
}
