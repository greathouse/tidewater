import greenmoonsoftware.es.Bus
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.web.Application
import greenmoonsoftware.tidewater.web.context.commands.PipelineContextQuery
import greenmoonsoftware.tidewater.web.context.view.PipelineContextViewRepository
import greenmoonsoftware.tidewater.web.pipeline.commands.PipelineCommandService
import greenmoonsoftware.tidewater.web.pipeline.commands.PipelineQuery
import greenmoonsoftware.tidewater.web.pipeline.view.PipelineViewRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.ConfigFileApplicationContextInitializer
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

@ContextConfiguration(classes = [Application], initializers = ConfigFileApplicationContextInitializer)
@ActiveProfiles('test')
class AbstractTidewaterIntegrationTests extends AbstractTestNGSpringContextTests {
    @Autowired Bus<Event, EventSubscriber> eventBus
    @Autowired PipelineCommandService pipelineCommandService
    @Autowired PipelineViewRepository pipelineViewRepository
    @Autowired PipelineQuery pipelineQuery
    @Autowired PipelineContextQuery pipelineContextQuery
    @Autowired PipelineContextViewRepository pipelineContextViewRepository
}
