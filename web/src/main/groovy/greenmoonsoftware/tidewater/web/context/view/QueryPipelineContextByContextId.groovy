package greenmoonsoftware.tidewater.web.context.view
import greenmoonsoftware.tidewater.context.ContextId
import greenmoonsoftware.tidewater.web.context.PipelineContextStatus
import groovy.sql.Sql

import javax.sql.DataSource

class QueryPipelineContextByContextId {
    private final DataSource ds
    private final Sql sql

    QueryPipelineContextByContextId(DataSource d) {
        ds = d
        sql = new Sql(ds)
    }

    PipelineContextView getByContextId(ContextId contextId) {
        def row = sql.firstRow("""
                select pipelineName,
                       contextId,
                       status,
                       startTime,
                       endTime
                from PipelineContext
                where contextId = ${contextId.id}
        """)
        return new PipelineContextView(
                pipelineName: row.pipelineName,
                contextId: new ContextId(row.contextId as String),
                status: PipelineContextStatus.fromValue(row.status as int),
                startTime: row.startTime.toInstant(),
                endTime: row.endTime.toInstant()
        )
    }
}
