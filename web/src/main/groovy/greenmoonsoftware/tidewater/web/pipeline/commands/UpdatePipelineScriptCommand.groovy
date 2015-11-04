package greenmoonsoftware.tidewater.web.pipeline.commands

import greenmoonsoftware.es.command.Command
import groovy.transform.Immutable

@Immutable
class UpdatePipelineScriptCommand implements Command {
    String name
    String script

    @Override
    String getAggregateId() {
        name
    }
}
