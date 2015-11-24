package greenmoonsoftware.tidewater.web.scripts.execute
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.run.RunContext
import greenmoonsoftware.tidewater.runtime.StdoutLoggingSubscriber
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class ExecuteService {
    @Autowired SimpMessagingTemplate template

    Context execute(String scriptText) {
        def context = new RunContext()
        context.addEventSubscribers(new StdoutLoggingSubscriber(), new LogEventHandler(context, template))
        context.execute(scriptText)
        return context
    }
}
