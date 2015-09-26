package greenmoonsoftware.tidewater.web.pipeline.runs.commands
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreConfiguration
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreQuery
import greenmoonsoftware.tidewater.json.JsonEventSerializer

import javax.sql.DataSource

class PipelineRunQuery extends JdbcStoreQuery<PipelineRunAggregate> {
    PipelineRunQuery(JdbcStoreConfiguration config, DataSource ds) {
        super(config, ds, new JsonEventSerializer())
    }

    @Override
    protected PipelineRunAggregate create() {
        return new PipelineRunAggregate()
    }
}