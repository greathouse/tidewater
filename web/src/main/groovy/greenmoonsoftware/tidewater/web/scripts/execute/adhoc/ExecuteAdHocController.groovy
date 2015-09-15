package greenmoonsoftware.tidewater.web.scripts.execute.adhoc

import greenmoonsoftware.tidewater.web.scripts.execute.ExecuteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class ExecuteAdHocController {
    @Autowired ExecuteService executeService

    @RequestMapping(value = '/execute', method = RequestMethod.POST)
    String post(@ModelAttribute ExecuteBody model, ModelMap response) {
        def context = executeService.execute(model.script)
        response.addAttribute('contextId', context.attributes.id)
        return 'execute/liveLog'
    }

    public static class ExecuteBody {
        String script
    }
}
