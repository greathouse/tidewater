package greenmoonsoftware.tidewater.web.context.view.retry

import greenmoonsoftware.tidewater.context.ContextId
import greenmoonsoftware.tidewater.restart.RestartContext
import greenmoonsoftware.tidewater.web.events.WebsocketSendEventSubscriber
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RetryContextController {
    @Autowired WebsocketSendEventSubscriber subscriber

    @RequestMapping(value = '/contexts/{contextId}/retry')
    ResponseEntity index(@PathVariable('contextId') String contextId) {
        new RestartContext(new ContextId(contextId))
            .addEventSubscribers(subscriber)
            .restart()
        return new ResponseEntity(HttpStatus.ACCEPTED)
    }
}
