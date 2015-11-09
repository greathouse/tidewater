package greenmoonsoftware.tidewater.web.pipeline.view.update
import greenmoonsoftware.tidewater.web.pipeline.commands.PipelineCommandService
import greenmoonsoftware.tidewater.web.pipeline.commands.UpdatePipelineScriptCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class UpdateScriptController {
    @Autowired PipelineCommandService service

    @RequestMapping(value = '/pipelines/{pipelineName}/updateScript', method = RequestMethod.PATCH)
    ResponseEntity index(@PathVariable('pipelineName') String pipelineName, @RequestBody Body body) {
        service.execute(new UpdatePipelineScriptCommand(pipelineName, body.script))
        return new ResponseEntity(HttpStatus.ACCEPTED)
    }

    static class Body {
        String script
    }
}
