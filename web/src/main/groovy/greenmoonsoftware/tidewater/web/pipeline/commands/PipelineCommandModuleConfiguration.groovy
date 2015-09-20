package greenmoonsoftware.tidewater.web.pipeline.commands

import greenmoonsoftware.es.Bus
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.web.pipeline.PipelineEventStoreConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PipelineCommandModuleConfiguration {
    @Autowired Bus<Event, EventSubscriber> eventBus
    @Autowired PipelineEventStoreConfiguration storeConfiguration

    @Bean
    CommandService pipelineService() { new CommandService(eventBus, storeConfiguration) }
}
