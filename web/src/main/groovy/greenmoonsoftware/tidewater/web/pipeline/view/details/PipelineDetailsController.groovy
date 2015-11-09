package greenmoonsoftware.tidewater.web.pipeline.view.details
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.tidewater.web.pipeline.PipelineEventStoreConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PipelineDetailsController {
    @Autowired PipelineEventStoreConfiguration c

    @RequestMapping(value = '/pipelines/{pipelineName}/events')
    List<Event> events(@PathVariable('pipelineName') String pipelineName) {
        def query = new PipelineDetailsEventStoreQuery(c.toConfiguration(), c.datasource)
        def aggregate = query.retrieve(pipelineName)
        return aggregate.events
    }
}
