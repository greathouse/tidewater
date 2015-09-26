package greenmoonsoftware.tidewater.web.context

import greenmoonsoftware.tidewater.web.context.commands.PipelineContextQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.sql.DataSource

@Configuration
class ContextModuleConfiguration {
    @Autowired DataSource ds

    @Bean
    PipelineContextEventStoreConfiguration pipelineRunEventStoreConfiguration() {
        new PipelineContextEventStoreConfiguration(ds)
    }

    @Bean
    PipelineContextQuery pipelineRunQuery() {
        new PipelineContextQuery(pipelineRunEventStoreConfiguration().toConfiguration(), ds)
    }
}
