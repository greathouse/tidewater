package greenmoonsoftware.tidewater.web.pipeline

import greenmoonsoftware.tidewater.web.pipeline.view.PipelineView
import greenmoonsoftware.tidewater.web.pipeline.view.PipelineViewQueryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class PipelineListController {
    @Autowired PipelineViewQueryService query

    @RequestMapping(value = '/pipelines', method = RequestMethod.GET)
    List<PipelineView> index() {
        query.pipelines
    }
}
