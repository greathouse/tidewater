package greenmoonsoftware.tidewater.web.pipeline.runs

import greenmoonsoftware.es.command.AggregateCommandApplier
import greenmoonsoftware.tidewater.config.ContextId
import greenmoonsoftware.tidewater.web.pipeline.runs.commands.StartPipelineRunCommand
import greenmoonsoftware.tidewater.web.pipeline.runs.events.PipelineRunStartedEvent
import org.testng.annotations.Test

import java.time.Instant

class PipelineRunAggregateTests {
    @Test
    void givenPipelineRunStartedCommand_shouldReturnPipelineRunStartedEvent() {
        def aggregate = new PipelineRunAggregate()

        def pipelineName = UUID.randomUUID().toString()
        def contextId = new ContextId(UUID.randomUUID().toString())
        def script = UUID.randomUUID().toString()
        def start = Instant.now()
        def actual = AggregateCommandApplier.apply(aggregate, new StartPipelineRunCommand(pipelineName, contextId, script, start))

        assert actual.size() == 1
        def aEvent = actual[0] as PipelineRunStartedEvent
        assert aEvent.pipelineName == pipelineName
        assert aEvent.contextId == contextId
        assert aEvent.script == script
        assert aEvent.start == start
    }
}
