package greenmoonsoftware.tidewater.web.scripts.execute.adhoc

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class DefineAdHocController {
    @RequestMapping(value = '/execute', method = RequestMethod.GET)
    String get(Map<String, Object> model) {
        return 'execute/index'
    }
}
