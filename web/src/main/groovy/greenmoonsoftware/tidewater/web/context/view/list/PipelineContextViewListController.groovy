package greenmoonsoftware.tidewater.web.context.view.list

import greenmoonsoftware.tidewater.web.context.view.PipelineContextView
import greenmoonsoftware.tidewater.web.context.view.PipelineContextViewRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PipelineContextViewListController {
    @Autowired private PipelineContextViewRepository repository

    @RequestMapping(value = '/pipelines/{pipelineName}/contexts')
    List<PipelineContextView> index(@PathVariable('pipelineName') String pipelineName) {
        repository.findByPipelineName(pipelineName)
    }
}
