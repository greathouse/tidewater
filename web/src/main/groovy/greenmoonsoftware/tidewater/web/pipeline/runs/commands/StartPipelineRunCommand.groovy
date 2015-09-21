package greenmoonsoftware.tidewater.web.pipeline.runs.commands
import greenmoonsoftware.es.command.Command
import greenmoonsoftware.tidewater.config.ContextId

import java.time.Instant

class StartPipelineRunCommand implements Command {
    String pipelineName
    ContextId contextId
    String script
    Instant start

    StartPipelineRunCommand(String pipelineName, ContextId id, String script, Instant start) {
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
