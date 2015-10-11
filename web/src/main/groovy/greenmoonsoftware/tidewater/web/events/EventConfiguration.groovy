package greenmoonsoftware.tidewater.web.events
import greenmoonsoftware.es.event.SimpleEventBus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.SimpMessagingTemplate

import javax.annotation.PostConstruct

@Configuration
class EventConfiguration {
    @Autowired SimpMessagingTemplate messagingTemplate

    @Bean
    SimpleEventBus simpleEventBus() {
        new SimpleEventBus()
    }

    @PostConstruct
    void websocketSendEventSubscriber() {
        simpleEventBus().register(new WebsocketSendEventSubscriber(messagingTemplate))
    }
}
