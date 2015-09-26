package greenmoonsoftware.tidewater.web.pipeline.view

import greenmoonsoftware.es.Bus
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.annotation.PostConstruct
import javax.sql.DataSource

@Configuration
class PipelineViewModuleConfiguration {
    @Autowired Bus<Event, EventSubscriber> eventBus
    @Autowired DataSource ds

    @Bean
    PipelineViewQueryService queryService() {
        new JdbcPipelineViewQueryService(ds)
    }

    @PostConstruct
    void postConstruct() {
        eventBus.register(new PipelineViewEventSubscriber(ds))
    }
}
