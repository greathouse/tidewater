package greenmoonsoftware.tidewater.web.pipeline.commands

import greenmoonsoftware.tidewater.config.Context
import greenmoonsoftware.tidewater.config.ContextAttributes
import greenmoonsoftware.tidewater.config.ContextId
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineStartedEvent
import org.testng.annotations.Test

class StartPipelineCommandTests {
    @Test
    void shouldRaisePipelineStartedEvent() {
        def expectedContextId = new ContextId(UUID.randomUUID().toString())
        def expectedName = UUID.randomUUID().toString()

        def actual = new StartPipelineCommand(expectedName, new DummyExecutingContext(setupDummyContextWithId(expectedContextId))).execute()

        assert actual.size() == 1
        def aEvent = actual[0] as PipelineStartedEvent
        assert aEvent.aggregateId == expectedName
        assert aEvent.start
        assert aEvent.contextId == expectedContextId
    }

    private Context setupDummyContextWithId(ContextId id) {
        [getAttributes: { new ContextAttributes(id) }] as Context
    }

    class DummyExecutingContext implements StartPipelineCommand.ExecutingContext {
        private final Context returnedContext
        List<String> scriptsExecuted = []

        DummyExecutingContext(Context returnContext) {
            this.returnedContext = returnContext
        }

        @Override
        Context execute(String script) {
            scriptsExecuted << script
            return returnedContext
        }
    }
}
