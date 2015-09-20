package greenmoonsoftware.tidewater.web.pipeline.commands
import greenmoonsoftware.es.event.SimpleEventBus
import greenmoonsoftware.tidewater.web.pipeline.PipelineEventStoreConfiguration
import org.flywaydb.core.Flyway
import org.testng.annotations.Test

import javax.sql.DataSource
import java.nio.file.Files

class PipelineServiceTests {
    @Test
    void givenCreatePipelineCommand_shouldCreateNewPipeline() {
        def storeConfiguration = new PipelineEventStoreConfiguration(Files.createTempDirectory('PipelineServiceTests').toString())
        migrate(storeConfiguration.datasource)
        def bus = new SimpleEventBus()
        def service = new CommandService(bus, storeConfiguration)

        def expectedName = UUID.randomUUID().toString()
        def expectedScript = UUID.randomUUID().toString()
        service.execute(new CreatePipelineCommand(expectedName, expectedScript))

        def query = new CommandQuery(storeConfiguration.toConfiguration(), storeConfiguration.datasource)
        def actual = query.retrieve(expectedName)
        assert actual.id == expectedName
        assert actual.script == expectedScript
    }

    private void migrate(DataSource ds) {
        def flyway = new Flyway()
        flyway.dataSource = ds
        flyway.migrate()
    }
}
