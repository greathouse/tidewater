package greenmoonsoftware.tidewater.web.pipeline

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class PipelineCreateController {
    @RequestMapping(value="/pipeline", method = RequestMethod.POST)
    ResponseEntity index(@RequestBody Body body) {
        println body.name + " " + body.script
        return new ResponseEntity(HttpStatus.NO_CONTENT)
    }

    public static class Body {
        String name
        String script
    }
}
