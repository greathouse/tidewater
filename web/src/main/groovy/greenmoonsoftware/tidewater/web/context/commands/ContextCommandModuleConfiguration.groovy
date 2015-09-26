package greenmoonsoftware.tidewater.web.context.commands

import greenmoonsoftware.es.Bus
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.web.context.PipelineContextEventStoreConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ContextCommandModuleConfiguration {
    @Autowired Bus<Event, EventSubscriber> eventBus
    @Autowired PipelineContextEventStoreConfiguration storeConfiguration

    @Bean
    PipelineContextCommandService pipelineRunCommandService() {
        new PipelineContextCommandService(eventBus, storeConfiguration)
    }
}
