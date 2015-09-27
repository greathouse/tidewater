import greenmoonsoftware.test.RetryableAssert
import greenmoonsoftware.tidewater.web.context.events.PipelineContextEndedEvent
import greenmoonsoftware.tidewater.web.context.events.PipelineContextStartedEvent
import greenmoonsoftware.tidewater.web.pipeline.commands.CreatePipelineCommand
import greenmoonsoftware.tidewater.web.pipeline.commands.StartPipelineCommand
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineCreatedEvent
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineStartedEvent
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class SuccessfulPipelineLifecycleTest extends AbstractTidewaterIntegrationTests {
    private IntegrationTestEventCollector eventCollector
    def pipelineName = "PipelineName-${UUID.randomUUID()}" as String
    String runAggregateId

    @BeforeMethod
    void onBeforeMethod() {
        eventCollector = new IntegrationTestEventCollector()
        eventBus.register(eventCollector)
    }

    @AfterMethod
    void onAfterMethod() {
        eventBus.unregister(eventCollector)
    }

    @Test
    void createPipeline() {
        def logMessage = 'Hello'
        pipelineCommandService.execute(new CreatePipelineCommand(pipelineName, """
            step test { println '${logMessage}' }
        """))
        assert findEventOfType(PipelineCreatedEvent)
    }

    @Test(dependsOnMethods = 'createPipeline')
    void startPipeline() {
        pipelineCommandService.execute(new StartPipelineCommand(pipelineName))
        assert findEventOfType(PipelineStartedEvent)
        def runEvent = findEventOfType(PipelineContextStartedEvent)
        assert runEvent
        runAggregateId = runEvent.aggregateId

        RetryableAssert.run {
            def contextEndEvent = findEventOfType(PipelineContextEndedEvent)
            assert contextEndEvent.aggregateId == runAggregateId
        }
    }

    @Test(dependsOnMethods = 'createPipeline')
    void canQueryForPipeline() {
        def pipeline = pipelineQuery.retrieve(pipelineName)
        assert pipeline
        assert pipeline.lastStartTime == null
    }

    @Test(dependsOnMethods = 'startPipeline')
    void canQueryForPipelineContext() {
        def run = pipelineContextQuery.retrieve(runAggregateId)
        assert run
    }

    private <T> T findEventOfType(Class<T> type) {
        eventCollector.events.find { it.type == type.canonicalName } as T
    }
}
