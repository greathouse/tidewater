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
        send(base(event) {
            step = event.step
            message = event.message
        })
    }

    void handle(StepSuccessfullyCompletedEvent event) {
        send(base(event) {
            step = event.step
            duration = event.duration
            endDate = event.endDate
        })
    }

    private send(message) {
        messagingTemplate.convertAndSend("/topic/greetings/${context.id}".toString(), message)
    }

    private base(Event e, Closure c) {
        def b = [
            id: e.id,
            aggregateId: e.aggregateId,
            eventDateTime: e.eventDateTime,
            type: e.type,
        ]
        c.delegate = b
        c.call(b)
        return b
    }
}
