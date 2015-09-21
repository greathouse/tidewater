package greenmoonsoftware.tidewater.web.pipeline.runs.commands
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreConfiguration
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreQuery
import greenmoonsoftware.tidewater.json.JsonEventSerializer
import greenmoonsoftware.tidewater.web.pipeline.runs.PipelineRunAggregate

import javax.sql.DataSource

class CommandQuery extends JdbcStoreQuery<PipelineRunAggregate> {
    CommandQuery(JdbcStoreConfiguration config, DataSource ds) {
        super(config, ds, new JsonEventSerializer())
    }

    @Override
    protected PipelineRunAggregate create() {
        return new PipelineRunAggregate()
    }
}