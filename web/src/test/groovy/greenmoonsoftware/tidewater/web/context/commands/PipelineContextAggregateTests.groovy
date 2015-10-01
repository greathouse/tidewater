package greenmoonsoftware.tidewater.web.context.commands

import greenmoonsoftware.es.command.AggregateCommandApplier
import greenmoonsoftware.es.command.Command
import greenmoonsoftware.es.command.CommandNotAllowedException
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventList
import greenmoonsoftware.tidewater.context.ContextId
import greenmoonsoftware.tidewater.web.context.PipelineContextStatus
import greenmoonsoftware.tidewater.web.context.events.*
import org.testng.Assert
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

import java.time.Instant

class PipelineContextAggregateTests {
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
        assert aEvent.start == start
    }

    @Test
    void givenSuccessfulCompletion_shouldReturnPipelineRunEndedEvent_withCompleteStatus() {
        def contextId = new ContextId(UUID.randomUUID().toString())
        def aggregate = createAggregate(contextId)
        def endTime = Instant.now()
        def actual = AggregateCommandApplier.apply(aggregate, new EndPipelineContextCommand(contextId, endTime))

        assert actual.size() == 1
        def aEvent = actual[0] as PipelineContextEndedEvent
        assert aEvent
        assert aEvent.aggregateId == contextId.id
        assert aEvent.endTime == endTime
        assert aEvent.status == PipelineContextStatus.COMPLETE
    }

    @DataProvider
    Object[][] abnormalCompletion() {[
            [ErrorPipelineContextCommand, PipelineContextErrorredEvent, PipelineContextStatus.ERROR],
            [FailPipelineContextCommand, PipelineContextFailedEvent, PipelineContextStatus.FAILURE],
            [AbortPipelineContextCommand, PipelineContextAbortedEvent, PipelineContextStatus.ABORT],
            [PausePipelineContextCommand, PipelinePausedEvent, PipelineContextStatus.PAUSE]
    ]}

    @Test(dataProvider = 'abnormalCompletion')
    void givenAbnormalTermination_shouldReturnCorrectStatus(Class<Command> commandClass, Class<Event> expectedEventClass, PipelineContextStatus expectedStatus) {
        def contextId = new ContextId(UUID.randomUUID().toString())
        def aggregate = createAggregate(contextId)
        executeCommand(aggregate, commandClass.newInstance(contextId)) { event ->
            assert event.class == expectedEventClass
        }
        def actual = AggregateCommandApplier.apply(aggregate, new EndPipelineContextCommand(contextId, Instant.now()))

        assert actual.size() == 1
        def aEvent = actual[0] as PipelineContextEndedEvent
        assert aEvent.status == expectedStatus
    }

    private Collection<Event> executeCommand(PipelineContextAggregate aggregate, Command command, Closure expectedEventClosure) {
        def raisedEvents = AggregateCommandApplier.apply(aggregate, command)
        assert raisedEvents.size() == 1
        expectedEventClosure.call(raisedEvents[0])
        aggregate.apply(new EventList().addAll(raisedEvents.toList()))
    }

    private PipelineContextAggregate createAggregate(ContextId c) {
        def aggregate = new PipelineContextAggregate()
        aggregate.apply(new EventList(new PipelineContextStartedEvent(UUID.randomUUID().toString(), c, '{}', Instant.now())))
        return aggregate
    }

    @DataProvider
    Object[][] invalidCommandsWhenAggregateIsCompleted() {[
            [ErrorPipelineContextCommand],
            [FailPipelineContextCommand],
            [AbortPipelineContextCommand],
            [PausePipelineContextCommand]
    ]}

    @Test(dataProvider = 'invalidCommandsWhenAggregateIsCompleted')
    void givenCompletedContext_whenInvalidCommand_shouldThrowCommandNotAllowedException(Class<Command> commandClass) {
        def id = new ContextId(UUID.randomUUID().toString())
        def aggregate = new PipelineContextAggregate(status: PipelineContextStatus.COMPLETE, id: id)
        try {
            AggregateCommandApplier.apply(aggregate, commandClass.newInstance(id))
            Assert.fail('Should have raised an exception')
        }
        catch (CommandNotAllowedException e) {
            assert true
        }
    }
}
