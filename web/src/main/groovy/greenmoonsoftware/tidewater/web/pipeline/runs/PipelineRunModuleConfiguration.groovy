package greenmoonsoftware.tidewater.web.pipeline.runs

import greenmoonsoftware.tidewater.web.pipeline.runs.commands.PipelineRunQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.sql.DataSource

@Configuration
class PipelineRunModuleConfiguration {
    @Autowired DataSource ds

    @Bean
    PipelineRunEventStoreConfiguration pipelineRunEventStoreConfiguration() {
        new PipelineRunEventStoreConfiguration(ds)
    }

    @Bean
    PipelineRunQuery pipelineRunQuery() {
        new PipelineRunQuery(pipelineRunEventStoreConfiguration().toConfiguration(), ds)
    }
}
