package greenmoonsoftware.tidewater.web.execute
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.config.Context
import greenmoonsoftware.tidewater.step.events.StepLogEvent
import greenmoonsoftware.tidewater.step.events.StepSuccessfullyCompletedEvent
import org.springframework.messaging.simp.SimpMessagingTemplate

class LogEventHandler implements EventSubscriber<Event> {
    private SimpMessagingTemplate messagingTemplate
    private Context context

    LogEventHandler(Context c, SimpMessagingTemplate t) {
        context = c
        messagingTemplate = t
    }

    @Override
    void onEvent(Event event) {
        EventApplier.apply(this, event)
    }

    void handle(StepLogEvent event) {
        send(event.message)
    }

    void handle(StepSuccessfullyCompletedEvent event) {
        send(event.step.name)
    }

    private send(String message) {
        messagingTemplate.convertAndSend("/topic/greetings/${context.id}".toString(), message)
    }
}
