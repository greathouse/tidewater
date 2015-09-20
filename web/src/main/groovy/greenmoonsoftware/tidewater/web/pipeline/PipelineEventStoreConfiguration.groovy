package greenmoonsoftware.tidewater.web.pipeline
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreConfiguration

import javax.sql.DataSource

class PipelineEventStoreConfiguration {
    final DataSource datasource
    final tablename = 'PipelineEvents'

    PipelineEventStoreConfiguration(DataSource d) {
        datasource = d
    }

    JdbcStoreConfiguration toConfiguration() {
        new JdbcStoreConfiguration(tablename)
    }
}
