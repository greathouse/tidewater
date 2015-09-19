package greenmoonsoftware.tidewater.web.pipeline.commands

import greenmoonsoftware.es.command.AggregateCommandApplier
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineCreatedEvent
import org.testng.Assert
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class PipelineAggregateTests {
    @Test
    void givenNewAggregate_whenCreatePipelineCommand_shouldRaisePipelineCreatedEvent() {
        def aggregate = new PipelineAggregate()

        def expectedName = UUID.randomUUID().toString()
        def expectedScript = UUID.randomUUID().toString()
        def actual = AggregateCommandApplier.apply(aggregate, new CreatePipelineCommand(expectedName, expectedScript))

        assert actual.size() == 1
        def aEvent = actual[0] as PipelineCreatedEvent
        assert aEvent.aggregateId == expectedName
        assert aEvent.scriptText == expectedScript
    }

    @Test
    void givenExistingAggregate_whenCreatePipelineCommand_shouldThrowException() {
        def name = 'InitialName'
        try {
            AggregateCommandApplier.apply(new PipelineAggregate(id: name), new CreatePipelineCommand(name, 'Doesnt Matter'))
            Assert.fail('Should have raised an exception')
        }
        catch (RuntimeException e) {
            assert true
        }
    }

    @DataProvider
    Object[][] invalidNames() {[
            [null], ['']
    ]}

    @Test(dataProvider = 'invalidNames')
    void givenCreatePipelineCommand_withNullOrEmptyName_shouldThrowException(String name) {
        try {
            AggregateCommandApplier.apply(new PipelineAggregate(), new CreatePipelineCommand(name, 'Doesnt Matter'))
            Assert.fail('Should have raised an exception')
        }
        catch (RuntimeException e) {
            assert true
        }
    }
}
