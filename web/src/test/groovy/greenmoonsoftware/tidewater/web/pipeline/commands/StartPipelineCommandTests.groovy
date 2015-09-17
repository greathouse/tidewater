package greenmoonsoftware.tidewater.web.pipeline.commands
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.tidewater.config.Context
import greenmoonsoftware.tidewater.config.ContextAttributes
import greenmoonsoftware.tidewater.config.ContextId
import greenmoonsoftware.tidewater.web.pipeline.PipelineQueryService
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineStartedEvent
import org.testng.annotations.Test

class StartPipelineCommandTests {
    @Test
    void shouldRaisePipelineStartedEvent() {
        def expectedContextId = new ContextId(UUID.randomUUID().toString())
        def expectedName = UUID.randomUUID().toString()

        def actual = execute(expectedName, new DummyExecutingContext(expectedContextId))

        assert actual.size() == 1
        def aEvent = actual[0] as PipelineStartedEvent
        assert aEvent.aggregateId == expectedName
        assert aEvent.start
        assert aEvent.contextId == expectedContextId
    }

    @Test
    void shouldCallExecuteWithAppropriateScript() {
        def expectedPipelineName = 'EXPECTED'
        def executingContext = new DummyExecutingContext(new ContextId(UUID.randomUUID().toString()))
        String expectedScript = "{EXPECTED: ${UUID.randomUUID()}"

        execute(expectedPipelineName, executingContext) {name ->
            return (name == expectedPipelineName) ? expectedScript : "Name No Match: ${name}"
        }

        assert executingContext.scriptsExecuted.size() == 1
        assert executingContext.scriptsExecuted[0] == expectedScript
    }

    private List<Event> execute(String expectedName, DummyExecutingContext executingContext) {
        execute(expectedName, executingContext) { 'Script content doesn\'t matter' }
    }

    private List<Event> execute(String expectedName, DummyExecutingContext executingContext, Closure getScript) {
        new StartPipelineCommand(
                expectedName,
                executingContext,
                setupQueryService(getScript)
        ).execute()
    }

    private static PipelineQueryService setupQueryService(Closure getScript) {
        [getScript: { name -> getScript.call(name) }] as PipelineQueryService
    }

    class DummyExecutingContext implements StartPipelineCommand.ExecutingContext {
        final ContextId contextId
        List<String> scriptsExecuted = []

        DummyExecutingContext(ContextId id) {
            contextId = id
        }

        @Override
        Context execute(String script) {
            scriptsExecuted << script
            return [getAttributes: { new ContextAttributes(contextId) }] as Context
        }
    }
}
