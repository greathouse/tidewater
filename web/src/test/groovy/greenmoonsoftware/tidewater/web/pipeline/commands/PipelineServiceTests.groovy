package greenmoonsoftware.tidewater.web.pipeline.commands
import greenmoonsoftware.es.event.SimpleEventBus
import greenmoonsoftware.tidewater.web.pipeline.DatabaseInitializer
import greenmoonsoftware.tidewater.web.pipeline.PipelineEventStoreConfiguration
import org.testng.annotations.Test

class PipelineServiceTests {
    @Test
    void givenCreatePipelineCommand_shouldCreateNewPipeline() {
        def storeConfiguration = new PipelineEventStoreConfiguration(DatabaseInitializer.initalize())
        def bus = new SimpleEventBus()
        def service = new PipelineCommandService(bus, storeConfiguration)

        def expectedName = UUID.randomUUID().toString()
        def expectedScript = UUID.randomUUID().toString()
        service.execute(new CreatePipelineCommand(expectedName, expectedScript))

        def query = new PipelineQuery(storeConfiguration.toConfiguration(), storeConfiguration.datasource)
        def actual = query.retrieve(expectedName)
        assert actual.id == expectedName
        assert actual.script == expectedScript
    }
}