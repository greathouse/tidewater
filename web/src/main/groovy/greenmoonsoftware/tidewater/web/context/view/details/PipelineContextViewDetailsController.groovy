package greenmoonsoftware.tidewater.web.context.view.details

import greenmoonsoftware.tidewater.context.ContextId
import greenmoonsoftware.tidewater.replay.ReplayRunner
import greenmoonsoftware.tidewater.web.context.view.PipelineContextViewRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PipelineContextViewDetailsController {
    @Autowired private PipelineContextViewRepository repository

    @RequestMapping(value = '/contexts/{contextId}')
    PipelineContextViewDetails index(@PathVariable('contextId') String contextId) {
        def replay = new ReplayRunner(new ContextId(contextId))
        def details = new PipelineContextViewDetails()
        replay.addEventSubscribers(new PipelineContextViewDetailsReplayEventSubscriber(details))
        replay.replay()

        def view = repository.findOne(contextId)
        details.pipelineName = view.pipelineName
        details.startTime = view.startTime
        details.endTime = view.endTime
        details.status = view.status

        return details
    }
}
