package greenmoonsoftware.tidewater.web.context.view.details

import greenmoonsoftware.tidewater.context.ContextId
import greenmoonsoftware.tidewater.replay.ReplayRunner
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PipelineContextViewDetailsController {
    @RequestMapping(value = '/contexts/{contextId}')
    PipelineContextViewDetails index(@PathVariable('contextId') String contextId) {
        def replay = new ReplayRunner(new ContextId(contextId))
        def details = new PipelineContextViewDetails()
        replay.addEventSubscribers(new PipelineContextViewDetailsReplayEventSubscriber(details))
        replay.replay()
        return details
    }
}
