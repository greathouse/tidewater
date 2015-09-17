package greenmoonsoftware.tidewater.web.pipeline.commands
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.tidewater.config.Context
import greenmoonsoftware.tidewater.config.ContextId
import greenmoonsoftware.tidewater.config.NewContext
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineStartedEvent

import java.time.Instant

class StartPipelineCommand {
    private final String name
    private final ExecutingContext executingContext

    StartPipelineCommand(String name) {
        this(name, new DefaultExecutingContext())
    }

    StartPipelineCommand(String name, ExecutingContext e) {
        this.name = name
        executingContext = e
    }

    List<Event> execute() {
        def start = Instant.now()
        def context = executingContext.execute('')
        [new PipelineStartedEvent(name, context.attributes.id, start)]
    }

    String getName() { name }
    ContextId getContextId() { contextId }

    static interface ExecutingContext {
        Context execute(String script)
    }

    static class DefaultExecutingContext implements ExecutingContext {
        @Override
        Context execute(String script) {
            def c = new NewContext()
            c.execute(script)
            return c
        }
    }
}
