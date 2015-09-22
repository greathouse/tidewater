import greenmoonsoftware.es.Bus
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.web.Application
import greenmoonsoftware.tidewater.web.pipeline.commands.CreatePipelineCommand
import greenmoonsoftware.tidewater.web.pipeline.commands.PipelineCommandService
import greenmoonsoftware.tidewater.web.pipeline.commands.StartPipelineCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.ConfigFileApplicationContextInitializer
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test

@ContextConfiguration(classes = [Application], initializers = ConfigFileApplicationContextInitializer)
@ActiveProfiles('test')
class PipelineLifecycleTest extends AbstractTestNGSpringContextTests {
    @Autowired Bus<Event, EventSubscriber> eventBus
    @Autowired PipelineCommandService pipelineCommandService

    @Test
    void canary() {
        def eventCollector = new IntegrationTestEventCollector()
        eventBus.register(eventCollector)

        def pipelineName = UUID.randomUUID().toString()
        pipelineCommandService.execute(new CreatePipelineCommand(pipelineName, '{}'))

        assert eventCollector.events.size() == 1

        eventCollector.clear()
        pipelineCommandService.execute(new StartPipelineCommand(pipelineName))

        assert eventCollector.events.size() == 2
    }

}
