package greenmoonsoftware.tidewater.web.pipeline

import greenmoonsoftware.tidewater.web.pipeline.commands.CreatePipelineCommand
import greenmoonsoftware.tidewater.web.pipeline.commands.PipelineService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class PipelineCreateController {
    @Autowired PipelineService service

    @RequestMapping(value="/pipeline", method = RequestMethod.POST)
    ResponseEntity index(@RequestBody Body body) {
        service.execute(new CreatePipelineCommand(body.name, body.script))
        return new ResponseEntity(HttpStatus.NO_CONTENT)
    }

    public static class Body {
        String name
        String script
    }
}
