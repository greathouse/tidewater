package greenmoonsoftware.tidewater.web.context

import greenmoonsoftware.es.event.jdbcstore.JdbcStoreConfiguration

import javax.sql.DataSource

class PipelineContextEventStoreConfiguration {
    final DataSource datasource
    final tablename = 'PipelineRunEvents'

    PipelineContextEventStoreConfiguration(DataSource d) {
        datasource = d
    }

    JdbcStoreConfiguration toConfiguration() {
        new JdbcStoreConfiguration(tablename)
    }
}
