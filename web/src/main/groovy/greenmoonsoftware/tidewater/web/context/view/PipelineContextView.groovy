package greenmoonsoftware.tidewater.web.context.view

import greenmoonsoftware.tidewater.context.ContextId
import greenmoonsoftware.tidewater.web.context.PipelineContextStatus

import java.time.Instant

class PipelineContextView {
    String pipelineName
    ContextId contextId
    PipelineContextStatus status
    Instant startTime
    Instant endTime
}
