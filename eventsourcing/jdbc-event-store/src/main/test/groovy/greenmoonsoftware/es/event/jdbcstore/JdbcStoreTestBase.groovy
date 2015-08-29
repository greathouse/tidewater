package greenmoonsoftware.es.event.jdbcstore

import greenmoonsoftware.es.event.SimpleEventBus
import groovy.sql.Sql
import org.h2.jdbcx.JdbcDataSource
import org.testng.annotations.BeforeMethod

class JdbcStoreTestBase {
    protected JdbcDataSource datasource
    protected Sql sql
    protected String tablename = 'GMS_EVENTS'
    JdbcStoreEventSubscriber subscriber
    protected SimpleEventBus eventBus

    @BeforeMethod
    void onSetup() {
        setupDatabase()
        createAndRegisterSubscriber()
    }

    private void setupDatabase() {
        datasource = new JdbcDataSource()
        datasource.with {
            user = 'testuser'
            url = "jdbc:h2:mem:${UUID.randomUUID().toString()};DB_CLOSE_DELAY=-1"
        }
        sql = new Sql(datasource)
        sql.execute("""
            CREATE TABLE if not exists ${tablename} (
                id VARCHAR,
                aggregateId VARCHAR,
                eventType VARCHAR,
                eventDateTime TIMESTAMP,
                savedTimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                data BLOB
            )
        """.toString())
    }

    private void createAndRegisterSubscriber() {
        eventBus = new SimpleEventBus()
        subscriber = new JdbcStoreEventSubscriber(new JdbcStoreConfiguration(tablename), datasource)
        eventBus.register(subscriber)
    }
}
