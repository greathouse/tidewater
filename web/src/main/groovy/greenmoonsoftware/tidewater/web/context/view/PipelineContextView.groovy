package greenmoonsoftware.tidewater.web.context.view
import greenmoonsoftware.tidewater.context.ContextId
import greenmoonsoftware.tidewater.web.context.PipelineContextStatus

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class PipelineContextView {
    @Id ContextId contextId
    String pipelineName
    PipelineContextStatus status
    Date startTime
    Date endTime
}
