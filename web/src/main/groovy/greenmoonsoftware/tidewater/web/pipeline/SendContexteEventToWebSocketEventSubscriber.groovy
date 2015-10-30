package greenmoonsoftware.tidewater.web.pipeline

import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import org.springframework.messaging.simp.SimpMessagingTemplate

class SendContexteEventToWebSocketEventSubscriber implements EventSubscriber<Event> {
    private final SimpMessagingTemplate messagingTemplate

    SendContexteEventToWebSocketEventSubscriber(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate
    }

    @Override
    void onEvent(Event event) {
        messagingTemplate.convertAndSend('/topic/events'.toString(), event)
    }
}
