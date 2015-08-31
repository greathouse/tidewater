package greenmoonsoftware.tidewater.web.archive

import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.replay.ReplayContext
import greenmoonsoftware.tidewater.step.StepLogEvent
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class LogController {
    @RequestMapping("/archive")
    String index(Map<String, Object> model) {
        def replay = new ReplayContext('2015-08-30_22-02-00')
        def log = ''
        replay.addEventSubscribers(new EventSubscriber<Event>() {
            @Override
            void onEvent(Event event) {
                EventApplier.apply(this, event)
            }

            private void handle(StepLogEvent event) {
                log += "${event.message}\n"
            }
        })
        replay.replay()
        model.put("log", log)
        return "archive/index"
    }
}
