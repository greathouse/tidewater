package greenmoonsoftware.tidewater.web.pipeline.runs.commands

import greenmoonsoftware.es.Bus
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.web.pipeline.runs.PipelineRunEventStoreConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PipelineRunsCommandModuleConfiguration {
    @Autowired Bus<Event, EventSubscriber> eventBus
    @Autowired PipelineRunEventStoreConfiguration storeConfiguration

    @Bean
    PipelineRunCommandService pipelineRunCommandService() {
        new PipelineRunCommandService(eventBus, storeConfiguration)
    }
}