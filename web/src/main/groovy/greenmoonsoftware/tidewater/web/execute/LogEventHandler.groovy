package greenmoonsoftware.tidewater.web.execute
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.step.events.StepLogEvent
import org.springframework.messaging.simp.SimpMessagingTemplate

class LogEventHandler implements EventSubscriber<Event> {
    private SimpMessagingTemplate messagingTemplate

    LogEventHandler(SimpMessagingTemplate t) {
        messagingTemplate = t
    }

    @Override
    void onEvent(Event event) {
        EventApplier.apply(this, event)
    }

    void handle(StepLogEvent event) {
        messagingTemplate.convertAndSend('/topic/greetings', event.message)
    }
}
