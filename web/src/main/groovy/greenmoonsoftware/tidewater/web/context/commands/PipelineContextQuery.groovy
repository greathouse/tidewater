package greenmoonsoftware.tidewater.web.context.commands
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreConfiguration
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreQuery
import greenmoonsoftware.tidewater.json.JsonEventSerializer

import javax.sql.DataSource

class PipelineContextQuery extends JdbcStoreQuery<PipelineContextAggregate> {
    PipelineContextQuery(JdbcStoreConfiguration config, DataSource ds) {
        super(config, ds, new JsonEventSerializer())
    }

    @Override
    protected PipelineContextAggregate create() {
        return new PipelineContextAggregate()
    }
}