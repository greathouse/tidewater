import greenmoonsoftware.test.RetryableAssert
import greenmoonsoftware.tidewater.web.context.PipelineContextStatus
import greenmoonsoftware.tidewater.web.context.events.PipelineContextEndedEvent
import greenmoonsoftware.tidewater.web.context.events.PipelineContextStartedEvent
import greenmoonsoftware.tidewater.web.pipeline.commands.CreatePipelineCommand
import greenmoonsoftware.tidewater.web.pipeline.commands.StartPipelineCommand
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineCreatedEvent
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineStartedEvent
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class FailPipelineLifecycleTest extends AbstractTidewaterIntegrationTests {
    private IntegrationTestEventCollector eventCollector
    def pipelineName = "PipelineName-${UUID.randomUUID()}" as String
    String runAggregateId

    @BeforeMethod
    void onBeforeMethod() {
        eventCollector = new IntegrationTestEventCollector()
        eventBus.register(eventCollector)

        pipelineCommandService.execute(new CreatePipelineCommand(pipelineName, """
            step test { return false }
        """))
        assert findEventOfType(PipelineCreatedEvent)
    }

    @AfterMethod
    void onAfterMethod() {
        eventBus.unregister(eventCollector)
    }

    @Test
    void startPipeline() {
        pipelineCommandService.execute(new StartPipelineCommand(pipelineName))
        assert findEventOfType(PipelineStartedEvent)
        def runEvent = findEventOfType(PipelineContextStartedEvent)
        assert runEvent
        runAggregateId = runEvent.aggregateId

        RetryableAssert.run {
            def contextEndEvent = findEventOfType(PipelineContextEndedEvent)
            assert contextEndEvent.aggregateId == runAggregateId
            assert contextEndEvent.status == PipelineContextStatus.FAILURE
        }
    }

    private <T> T findEventOfType(Class<T> type) {
        eventCollector.events.find { it.type == type.canonicalName } as T
    }

}
