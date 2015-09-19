package greenmoonsoftware.tidewater.web.pipeline

import greenmoonsoftware.es.event.jdbcstore.JdbcStoreConfiguration
import greenmoonsoftware.tidewater.config.Tidewater
import org.h2.jdbcx.JdbcDataSource

import javax.sql.DataSource

class PipelineEventStoreConfiguration {
    final DataSource datasource = new JdbcDataSource()
    final tablename = 'PipelineEvents'

    PipelineEventStoreConfiguration() {
        this(Tidewater.WORKSPACE_ROOT)
    }

    PipelineEventStoreConfiguration(String rootDir) {
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
