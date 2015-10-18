package greenmoonsoftware.tidewater.web.context.view

import greenmoonsoftware.tidewater.web.context.PipelineContextStatus

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class PipelineContextView {
    @Id String contextId
    String pipelineName
    PipelineContextStatus status
    Date startTime
    Date endTime
}
