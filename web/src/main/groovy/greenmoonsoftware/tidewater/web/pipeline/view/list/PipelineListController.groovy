package greenmoonsoftware.tidewater.web.pipeline.view.list
import greenmoonsoftware.tidewater.web.pipeline.view.PipelineView
import greenmoonsoftware.tidewater.web.pipeline.view.PipelineViewRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class PipelineListController {
    @Autowired PipelineViewRepository repository

    @RequestMapping(value = '/pipelines', method = RequestMethod.GET)
    List<PipelineView> index() {
        repository.findAll().asList()
    }
}
