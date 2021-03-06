package greenmoonsoftware.tidewater.web.pipeline.commands

import greenmoonsoftware.es.command.Command
import groovy.transform.Immutable

@Immutable
class StartPipelineCommand implements Command {
    String name

    @Override
    String getAggregateId() {
        name
    }
}
