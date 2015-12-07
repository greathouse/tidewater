package greenmoonsoftware.tidewater.web.threads

import greenmoonsoftware.tidewater.web.PipelineContextContainer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ListController {
    @Autowired private PipelineContextContainer container

    @RequestMapping('/threads')
    List index() {
        container.getRunningContextIds().asList()
    }
}
