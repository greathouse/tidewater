package greenmoonsoftware.tidewater.web.pipeline
import greenmoonsoftware.es.Bus
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.web.pipeline.commands.CommandService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.sql.DataSource

@Configuration
class PipelineModuleConfiguration {
    @Autowired Bus<Event, EventSubscriber> eventBus
    @Autowired DataSource ds

    @Bean
    PipelineEventStoreConfiguration eventStoreConfiguration() { new PipelineEventStoreConfiguration(ds) }

    @Bean
    CommandService pipelineService() { new CommandService(eventBus, eventStoreConfiguration()) }
}
