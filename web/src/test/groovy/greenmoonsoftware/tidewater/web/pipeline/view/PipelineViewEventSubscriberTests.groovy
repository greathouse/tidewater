package greenmoonsoftware.tidewater.web.pipeline.view
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.tidewater.config.ContextId
import greenmoonsoftware.tidewater.web.pipeline.DatabaseInitializer
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineCreatedEvent
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineStartedEvent
import groovy.sql.Sql
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

import javax.sql.DataSource
import java.time.Instant

class PipelineViewEventSubscriberTests {
    DataSource datasource
    Sql sql
    PipelineViewEventSubscriber subscriber

    String pipelineName
    String pipelineScript

    @BeforeClass
    void init() {
        datasource = DatabaseInitializer.initalize()
        subscriber = new PipelineViewEventSubscriber(datasource)
        sql = new Sql(datasource)
        pipelineName = UUID.randomUUID().toString()
        pipelineScript = UUID.randomUUID().toString()
    }

    @Test
    void givenPipelineCreatedEvent_shouldInsertPipelineToDatabase() {
        subscriber.onEvent(new PipelineCreatedEvent(pipelineName, pipelineScript) as Event)

        assert 1 == sql.firstRow("select count(*) cnt from pipeline where name = ${pipelineName}").cnt
        sql.eachRow("select * from pipeline where name = ${pipelineName}") { row ->
            //The eachRow method is required to read the CLOB value.
            //Otherwise an "object already closed" exception will occur
            assert row.name == pipelineName
            assert row.script.asciiStream.text == pipelineScript
        }
    }

    @Test(dependsOnMethods = 'givenPipelineCreatedEvent_shouldInsertPipelineToDatabase')
    void givenPipelineStartedEvent_shouldInsertPipelineRunTable() {
        def contextId = new ContextId(UUID.randomUUID().toString())
        def start = Instant.now()
        subscriber.onEvent(new PipelineStartedEvent(pipelineName, contextId, pipelineScript, start))

        def actual = sql.firstRow("select * from PipelineRun where pipelineName = ${pipelineName}")
        assert actual
        assert actual.contextId == contextId.id
        assert actual.startTime.toInstant() == start
        assert actual.endTime == null
    }
}
