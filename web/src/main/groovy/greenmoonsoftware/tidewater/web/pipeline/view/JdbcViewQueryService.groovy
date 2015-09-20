package greenmoonsoftware.tidewater.web.pipeline.view

import groovy.sql.Sql

import javax.sql.DataSource

class JdbcViewQueryService implements ViewQueryService {
    private final Sql sql

    JdbcViewQueryService(DataSource ds) {
        sql = new Sql(ds)
    }

    @Override
    String getScript(String name) {
        String script
        sql.eachRow("select script from Pipeline where name = ${name}") { row ->
            script = row.script.asciiStream.text
        }
        if (!script) {
            throw new PipelineNotFoundException("Unable to find pipeline with name \"${name}\"")
        }
        return script
    }
}
