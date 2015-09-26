package greenmoonsoftware.tidewater.web.context.view
import greenmoonsoftware.tidewater.config.ContextId
import groovy.sql.Sql

import javax.sql.DataSource

class PipelineRunViewQuery {
    private final DataSource ds
    private final Sql sql

    PipelineRunViewQuery(DataSource d) {
        ds = d
        sql = new Sql(ds)
    }

    PipelineContextView getByContextId(ContextId contextId) {
        def row = sql.firstRow("select pipelineName, contextId, script, startTime, endTime from PipelineRun where contextId = ${contextId.id}")
        return new PipelineContextView(
                pipelineName: row.pipelineName,
                contextId: new ContextId(row.contextId as String),
                script: row.script,
                startTime: row.startTime.toInstant()
        )
    }
}
