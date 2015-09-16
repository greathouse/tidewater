package greenmoonsoftware.tidewater.web.events

import greenmoonsoftware.es.event.SimpleEventBus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EventConfiguration {
    @Bean
    SimpleEventBus simpleEventBus() {
        new SimpleEventBus()
    }
}
