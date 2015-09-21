package greenmoonsoftware.tidewater.web.pipeline.runs

import greenmoonsoftware.es.event.jdbcstore.JdbcStoreConfiguration

import javax.sql.DataSource

class PipelineRunEventStoreConfiguration {
    final DataSource datasource
    final tablename = 'PipelineRunEvents'

    PipelineRunEventStoreConfiguration(DataSource d) {
        datasource = d
    }

    JdbcStoreConfiguration toConfiguration() {
        new JdbcStoreConfiguration(tablename)
    }
}
