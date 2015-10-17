package greenmoonsoftware.tidewater.web.context.view

import org.springframework.data.repository.CrudRepository

interface PipelineContextViewRepository extends CrudRepository<PipelineContextView, String> {
    PipelineContextView findOne(String contextId)
    List<PipelineContextView> findByPipelineName(String pipelineName)
}
