package greenmoonsoftware.tidewater.web.pipeline

import greenmoonsoftware.es.event.Event
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineCreatedEvent
import greenmoonsoftware.tidewater.web.pipeline.view.PipelineViewEventSubscriber
import groovy.sql.Sql
import org.flywaydb.core.Flyway
import org.h2.jdbcx.JdbcDataSource
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class PipelineViewEventSubscriberTests {
    JdbcDataSource datasource
    Sql sql
    PipelineViewEventSubscriber subscriber

    @BeforeClass
    void init() {
        initalizeDatabaseConnections()
        migrateDatabase(datasource)
        subscriber = new PipelineViewEventSubscriber(datasource)
    }

    private void initalizeDatabaseConnections() {
        datasource = new JdbcDataSource()
        datasource.url = 'jdbc:h2:~/test'
        datasource.user = 'sa'
        sql = new Sql(datasource)
    }

    private void migrateDatabase(JdbcDataSource datasource) {
        def flyway = new Flyway()
        flyway.dataSource = datasource
        flyway.migrate()
    }

    @Test
    void givenPipelineCreatedEvent_shouldInsertPipelineToDatabase() {
        def expectedName = UUID.randomUUID().toString()
        def expectedScript = UUID.randomUUID().toString()

        subscriber.onEvent(new PipelineCreatedEvent(expectedName, expectedScript) as Event)

        assert 1 == sql.firstRow("select count(*) cnt from pipeline where name = ${expectedName}").cnt
        sql.eachRow("select * from pipeline where name = ${expectedName}") { row ->
            //The eachRow method is required to read the CLOB value.
            //Otherwise an "object already closed" exception will occurr
            assert row.name == expectedName
            assert row.script.asciiStream.text == expectedScript
            assert row.lastSuccessfulRun == null
            assert row.lastFailure == null
        }
    }
}
