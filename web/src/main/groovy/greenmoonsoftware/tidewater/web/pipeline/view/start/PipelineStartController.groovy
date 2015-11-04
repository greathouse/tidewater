package greenmoonsoftware.tidewater.web.pipeline.view.start

import greenmoonsoftware.tidewater.web.pipeline.commands.PipelineCommandService
import greenmoonsoftware.tidewater.web.pipeline.commands.StartPipelineCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class PipelineStartController {
    @Autowired PipelineCommandService service

    @RequestMapping(value = '/pipelines/{pipelineName}/start', method = RequestMethod.GET)
    ResponseEntity index(@PathVariable('pipelineName') String pipelineName) {
        service.execute(new StartPipelineCommand(pipelineName))
        return new ResponseEntity(HttpStatus.OK)
    }
}
