package greenmoonsoftware.tidewater.web.scripts.execute

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class LiveLogController {
    @RequestMapping('/execute/log')
    String index() {
        'execute/liveLog'
    }
}
