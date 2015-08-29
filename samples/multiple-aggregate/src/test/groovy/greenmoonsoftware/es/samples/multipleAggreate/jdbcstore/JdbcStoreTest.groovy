package greenmoonsoftware.es.samples.multipleAggreate.jdbcstore
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreConfiguration
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreEventSubscriber
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreQuery
import greenmoonsoftware.es.samples.multipleAggreate.AggregateTestBase
import greenmoonsoftware.es.samples.user.User
import groovy.sql.Sql
import org.h2.jdbcx.JdbcDataSource
import org.testng.annotations.BeforeMethod

class JdbcStoreTest extends AggregateTestBase {
    protected JdbcDataSource datasource
    protected Sql sql
    protected String tablename = 'GMS_EVENTS'
    JdbcStoreEventSubscriber store
    JdbcStoreQuery<User> query

    @BeforeMethod
    final void onSetup() {
        setupDatabase()
        store = new JdbcStoreEventSubscriber(new JdbcStoreConfiguration(tablename), datasource)
    }

    @BeforeMethod
    void setupUserQuery() {
        query = new JdbcStoreQuery<User>(new JdbcStoreConfiguration(tablename),datasource) {
            @Override
            protected User create() {
                return new User()
            }
        }
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
}
