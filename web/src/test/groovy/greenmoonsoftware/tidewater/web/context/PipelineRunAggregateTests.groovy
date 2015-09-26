package greenmoonsoftware.tidewater.web.context

import greenmoonsoftware.es.command.AggregateCommandApplier
import greenmoonsoftware.es.event.EventList
import greenmoonsoftware.tidewater.config.ContextId
import greenmoonsoftware.tidewater.web.context.commands.EndPipelineRunCommand
import greenmoonsoftware.tidewater.web.context.commands.PipelineRunAggregate
import greenmoonsoftware.tidewater.web.context.commands.StartPipelineRunCommand
import greenmoonsoftware.tidewater.web.context.events.PipelineRunEndedEvent
import greenmoonsoftware.tidewater.web.context.events.PipelineRunStartedEvent
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

    @Test
    void givenEndPipelineRunCommand_shouldReturnPipelineRunEndedEvent() {
        def contextId = new ContextId(UUID.randomUUID().toString())
        def aggregate = createAggregate(contextId)
        def endTime = Instant.now()
        def actual = AggregateCommandApplier.apply(aggregate, new EndPipelineRunCommand(contextId, endTime))

        assert actual.size() == 1
        def aEvent = actual[0] as PipelineRunEndedEvent
        assert aEvent
        assert aEvent.aggregateId == contextId.id
        assert aEvent.endTime == endTime
    }

    private PipelineRunAggregate createAggregate(ContextId c) {
        def aggregate = new PipelineRunAggregate()
        aggregate.apply(new EventList(new PipelineRunStartedEvent(UUID.randomUUID().toString(), c, '{}', Instant.now())))
        return aggregate
    }
}
