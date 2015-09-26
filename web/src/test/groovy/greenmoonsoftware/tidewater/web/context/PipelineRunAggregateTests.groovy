package greenmoonsoftware.tidewater.web.context

import greenmoonsoftware.es.command.AggregateCommandApplier
import greenmoonsoftware.es.event.EventList
import greenmoonsoftware.tidewater.config.ContextId
import greenmoonsoftware.tidewater.web.context.commands.EndPipelineContextCommand
import greenmoonsoftware.tidewater.web.context.commands.PipelineContextAggregate
import greenmoonsoftware.tidewater.web.context.commands.StartPipelineContextCommand
import greenmoonsoftware.tidewater.web.context.events.PipelineContextEndedEvent
import greenmoonsoftware.tidewater.web.context.events.PipelineContextStartedEvent
import org.testng.annotations.Test

import java.time.Instant

class PipelineRunAggregateTests {
    @Test
    void givenPipelineRunStartedCommand_shouldReturnPipelineRunStartedEvent() {
        def aggregate = new PipelineContextAggregate()

        def pipelineName = UUID.randomUUID().toString()
        def contextId = new ContextId(UUID.randomUUID().toString())
        def script = UUID.randomUUID().toString()
        def start = Instant.now()
        def actual = AggregateCommandApplier.apply(aggregate, new StartPipelineContextCommand(pipelineName, contextId, script, start))

        assert actual.size() == 1
        def aEvent = actual[0] as PipelineContextStartedEvent
        assert aEvent.pipelineName == pipelineName
        assert aEvent.contextId == contextId
        assert aEvent.script == script
        assert aEvent.start == start
    }

    @Test
    void givenEndPipelineRunCommand_shouldReturnPipelineRunEndedEvent() {
        def contextId = new ContextId(UUID.randomUUID().toString())
        def aggregate = createAggregate(contextId)
        def endTime = Instant.now()
        def actual = AggregateCommandApplier.apply(aggregate, new EndPipelineContextCommand(contextId, endTime))

        assert actual.size() == 1
        def aEvent = actual[0] as PipelineContextEndedEvent
        assert aEvent
        assert aEvent.aggregateId == contextId.id
        assert aEvent.endTime == endTime
    }

    private PipelineContextAggregate createAggregate(ContextId c) {
        def aggregate = new PipelineContextAggregate()
        aggregate.apply(new EventList(new PipelineContextStartedEvent(UUID.randomUUID().toString(), c, '{}', Instant.now())))
        return aggregate
    }
}
