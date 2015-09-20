package greenmoonsoftware.tidewater.web.pipeline.view

import greenmoonsoftware.tidewater.web.pipeline.DatabaseInitializer
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineCreatedEvent
import org.testng.Assert
import org.testng.annotations.Test

class ViewTests {
    @Test
    void givenCreatePipelineEvent_shouldBeAbleToQueryForScript() {
        def ds = DatabaseInitializer.initalize()
        def eventSubscriber = new PipelineViewEventSubscriber(ds)
        def query = new JdbcViewQueryService(ds)

        def name = UUID.randomUUID().toString()
        def expectedScript = UUID.randomUUID().toString()
        eventSubscriber.onEvent(new PipelineCreatedEvent(name, expectedScript))

        def actualScript = query.getScript(name)
        assert actualScript == expectedScript
    }

    @Test
    void givenUnknownScript_shouldThrowNotFoundException() {
        def ds = DatabaseInitializer.initalize()
        def eventSubscriber = new PipelineViewEventSubscriber(ds)
        def query = new JdbcViewQueryService(ds)
        createPipeline(eventSubscriber) //negative pipeline

        try {
            query.getScript('Should not exist')
            Assert.fail('Should have raised an exception')
        }
        catch (PipelineNotFoundException e) {
            assert true
        }
    }

    void createPipeline(PipelineViewEventSubscriber s) {
        s.onEvent(new PipelineCreatedEvent(UUID.randomUUID().toString(), UUID.randomUUID().toString()))
    }
}
