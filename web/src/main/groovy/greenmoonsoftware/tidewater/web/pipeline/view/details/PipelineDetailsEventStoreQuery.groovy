package greenmoonsoftware.tidewater.web.pipeline.view.details

import greenmoonsoftware.es.event.jdbcstore.JdbcStoreConfiguration
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreQuery
import greenmoonsoftware.tidewater.json.JsonEventSerializer

import javax.sql.DataSource

class PipelineDetailsEventStoreQuery extends JdbcStoreQuery<PipelineEventDetails> {
    PipelineDetailsEventStoreQuery(JdbcStoreConfiguration config, DataSource ds) {
        super(config, ds, new JsonEventSerializer())
    }

    @Override
    protected PipelineEventDetails create() {
        return new PipelineEventDetails()
    }
}
