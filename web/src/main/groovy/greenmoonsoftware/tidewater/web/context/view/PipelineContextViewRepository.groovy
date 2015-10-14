package greenmoonsoftware.tidewater.web.context.view

import greenmoonsoftware.tidewater.context.ContextId
import org.springframework.data.repository.CrudRepository

interface PipelineContextViewRepository extends CrudRepository<PipelineContextView, String> {
    PipelineContextView findOne(String contextId)
    List<PipelineContextView> findByPipelineName(ContextId pipelineName)
}
