package greenmoonsoftware.tidewater.web.ping

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class PingController {
    @Autowired private SimpMessagingTemplate messagingTemplate

    @RequestMapping(value = '/ping', method = RequestMethod.GET)
    ResponseEntity index() {
        messagingTemplate.convertAndSend('/topic/greetings/ping', ['message':'pong'])
        return new ResponseEntity(HttpStatus.ACCEPTED)
    }
}
