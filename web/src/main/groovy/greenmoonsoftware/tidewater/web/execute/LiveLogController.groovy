package greenmoonsoftware.tidewater.web.execute

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class LiveLogController {

    @RequestMapping('/execute/log')
    String index() {
        'execute/liveLog'
    }

    @MessageMapping('/hello')
    @SendTo('/topic/greetings')
    String hello() throws Exception {
        Thread.sleep(3000); // simulated delay
        return 'hello'
    }
}
