package greenmoonsoftware.tidewater.web.pipeline.commands
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreConfiguration
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreQuery
import greenmoonsoftware.tidewater.json.JsonEventSerializer

import javax.sql.DataSource

class PipelineQuery extends JdbcStoreQuery<PipelineAggregate> {
    PipelineQuery(JdbcStoreConfiguration config, DataSource ds) {
        super(config, ds, new JsonEventSerializer())
    }

    @Override
    protected PipelineAggregate create() {
        return new PipelineAggregate()
    }
}
