package greenmoonsoftware.tidewater.web.pipeline
import greenmoonsoftware.es.Bus
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.web.context.commands.PipelineContextCommandService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.annotation.PostConstruct
import javax.sql.DataSource

@Configuration
class PipelineModuleConfiguration {
    @Autowired Bus<Event, EventSubscriber> eventBus
    @Autowired DataSource ds
    @Autowired PipelineContextCommandService pipelineRunCommandService

    @Bean
    PipelineEventStoreConfiguration eventStoreConfiguration() { new PipelineEventStoreConfiguration(ds) }

    @PostConstruct
    void postConstruct() {
        eventBus.register(new PipelineLifecycleManagementEventSubscriber(eventBus, pipelineRunCommandService))
    }
}
