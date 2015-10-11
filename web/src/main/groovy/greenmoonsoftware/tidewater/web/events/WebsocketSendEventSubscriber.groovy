package greenmoonsoftware.tidewater.web.events

import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import org.springframework.messaging.simp.SimpMessagingTemplate

class WebsocketSendEventSubscriber implements EventSubscriber<Event> {
    private final SimpMessagingTemplate messagingTemplate

    WebsocketSendEventSubscriber(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate
    }

    @Override
    void onEvent(Event event) {
        messagingTemplate.convertAndSend('/topic/events', event)
    }
}
