package greenmoonsoftware.tidewater.web.pipeline.commands

import greenmoonsoftware.es.command.Command
import groovy.transform.Immutable

@Immutable
class CreatePipelineCommand implements Command {
    String name
    String scriptText

    @Override
    String getAggregateId() {
        name
    }
}
