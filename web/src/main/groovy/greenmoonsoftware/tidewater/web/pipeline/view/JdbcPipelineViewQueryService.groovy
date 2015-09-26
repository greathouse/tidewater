package greenmoonsoftware.tidewater.web.pipeline.view

import groovy.sql.Sql

import javax.sql.DataSource

class JdbcPipelineViewQueryService implements PipelineViewQueryService {
    private final Sql sql

    JdbcPipelineViewQueryService(DataSource ds) {
        sql = new Sql(ds)
    }

    @Override
    String getScript(String name) {
        String script = null
        sql.eachRow("select script from Pipeline where name = ${name}") { row ->
            script = row.script.asciiStream.text
        }
        if (!script) {
            throw new PipelineNotFoundException()
        }
        return script
    }

    @Override
    List<PipelineView> getPipelines() {
        def rtn = []
        sql.eachRow("select name, script from Pipeline") { row ->
            rtn << new PipelineView(row.name, row.script.asciiStream.text)
        }
        return rtn
    }
}
