import greenmoonsoftware.es.Bus
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.web.Application
import greenmoonsoftware.tidewater.web.pipeline.commands.CreatePipelineCommand
import greenmoonsoftware.tidewater.web.pipeline.commands.PipelineCommandService
import greenmoonsoftware.tidewater.web.pipeline.commands.PipelineQuery
import greenmoonsoftware.tidewater.web.pipeline.commands.StartPipelineCommand
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineCreatedEvent
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineStartedEvent
import greenmoonsoftware.tidewater.web.pipeline.runs.commands.PipelineRunQuery
import greenmoonsoftware.tidewater.web.pipeline.runs.events.PipelineRunStartedEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.ConfigFileApplicationContextInitializer
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@ContextConfiguration(classes = [Application], initializers = ConfigFileApplicationContextInitializer)
@ActiveProfiles('test')
class PipelineLifecycleTest extends AbstractTestNGSpringContextTests {
    @Autowired Bus<Event, EventSubscriber> eventBus
    @Autowired PipelineCommandService pipelineCommandService
    @Autowired PipelineQuery pipelineQuery
    @Autowired PipelineRunQuery pipelineRunQuery

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
            step test(type: Groovy) { println '${logMessage}' }
        """))
        assert findEventOfType(PipelineCreatedEvent)
    }

    @Test(dependsOnMethods = 'createPipeline')
    void startPipeline() {
        pipelineCommandService.execute(new StartPipelineCommand(pipelineName))
        assert findEventOfType(PipelineStartedEvent)
        def runEvent = findEventOfType(PipelineRunStartedEvent)
        assert runEvent
        runAggregateId = runEvent.aggregateId
    }

    @Test(dependsOnMethods = 'createPipeline')
    void canQueryForPipeline() {
        def pipeline = pipelineQuery.retrieve(pipelineName)
        assert pipeline
        assert pipeline.lastStartTime == null
    }

    @Test(dependsOnMethods = 'startPipeline')
    void canQueryForPipelineRun() {
        def run = pipelineRunQuery.retrieve(runAggregateId)
        assert run
    }

    private <T> T findEventOfType(Class<T> type) {
        eventCollector.events.find { it.type == type.canonicalName } as T
    }
}
