package greenmoonsoftware.tidewater.web.context.view.details

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PipelineContextViewDetailsController {
    @RequestMapping(value = '/contexts/{contextId}')
    PipelineContextViewDetails index(@PathVariable('contextId') String contextId) {

    }
}
