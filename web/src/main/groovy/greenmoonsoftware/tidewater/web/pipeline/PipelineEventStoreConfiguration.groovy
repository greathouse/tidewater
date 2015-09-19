package greenmoonsoftware.tidewater.web.pipeline
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreConfiguration
import org.h2.jdbcx.JdbcDataSource

import javax.sql.DataSource

class PipelineEventStoreConfiguration {
    final DataSource datasource
    final tablename = 'PipelineEvents'

    PipelineEventStoreConfiguration(DataSource d) {
        datasource = d
    }

    PipelineEventStoreConfiguration(String rootDir) {
        datasource = new JdbcDataSource()
        initalizeDatasource(rootDir)
    }

    private String initalizeDatasource(rootDir) {
        datasource.with {
            user = 'testuser'
            url = "jdbc:h2:${rootDir}/web".toString()
        }
    }

    JdbcStoreConfiguration toConfiguration() {
        new JdbcStoreConfiguration(tablename)
    }
}
