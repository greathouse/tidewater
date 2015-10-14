package greenmoonsoftware.tidewater.web.context.view
import greenmoonsoftware.es.Bus
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

import javax.annotation.PostConstruct
import javax.sql.DataSource

@Configuration
class PipelineContextViewModuleConfiguration {
    @Autowired Bus<Event, EventSubscriber> eventBus
    @Autowired DataSource ds
    @Autowired PipelineContextViewRepository pipelineContextViewRepository

    @PostConstruct
    void postConstruct() {
        eventBus.register(new PipelineContextViewEventSubscriber(ds, pipelineContextViewRepository))
    }
}
