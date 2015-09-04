package greenmoonsoftware.tidewater.web.execute
import greenmoonsoftware.tidewater.config.Context
import greenmoonsoftware.tidewater.runtime.StdoutLoggingSubscriber
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class ExecuteController {
    @Autowired SimpMessagingTemplate template

    @RequestMapping(value = '/execute', method = RequestMethod.GET)
    String get(Map<String, Object> model) {
        return 'execute/index'
    }

    @RequestMapping(value = '/execute', method = RequestMethod.POST)
    String post(@ModelAttribute ExecuteBody model, ModelMap response) {
        println model.script
        def context = new Context()
        context.addEventSubscribers(new StdoutLoggingSubscriber(), new LogEventHandler(context, template))
        context.execute(model.script)

        response.addAttribute('contextId', context.id)
        return 'execute/liveLog'
    }

    public static class ExecuteBody {
        String script
    }
}
