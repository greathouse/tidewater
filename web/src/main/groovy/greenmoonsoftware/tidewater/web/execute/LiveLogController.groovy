package greenmoonsoftware.tidewater.web.execute

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class LiveLogController {

    @Autowired SimpMessagingTemplate template

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

    @RequestMapping('/p')
    String p() {
        template.convertAndSend('/topic/greetings', 'Ppppppppp')
        return 'index'
    }
}
