package greenmoonsoftware.tidewater.context

import greenmoonsoftware.es.event.jdbcstore.JdbcStoreConfiguration
import groovy.sql.Sql
import org.h2.jdbcx.JdbcDataSource

final class TidewaterEventStoreConfiguration {
    final File metaDirectory
    final datasource = new JdbcDataSource()
    final tablename = 'TW_EVENTS'

    TidewaterEventStoreConfiguration(File metaDirectory) {
        this.metaDirectory = metaDirectory
        setupDatabase()
    }

    private void setupDatabase() {
        datasource.with {
            user = 'testuser'
            url = "jdbc:h2:${metaDirectory.absolutePath}".toString()
        }
        def sql = new Sql(datasource)
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

    JdbcStoreConfiguration toConfiguration() {
        new JdbcStoreConfiguration(tablename)
    }
}
