package greenmoonsoftware.tidewater.web.pipeline

import org.flywaydb.core.Flyway
import org.h2.jdbcx.JdbcDataSource

import javax.sql.DataSource
import java.nio.file.Files

class DatabaseInitializer {
    static DataSource initalize() {
        def datasource = new JdbcDataSource()
        datasource.with {
            user = 'testuser'
            url = "jdbc:h2:${Files.createTempDirectory('PipelineServiceTests').toString()}/web".toString()
        }
        migrate(datasource)
        return datasource
    }

    private static void migrate(DataSource ds) {
        def flyway = new Flyway()
        flyway.dataSource = ds
        flyway.migrate()
    }
}
