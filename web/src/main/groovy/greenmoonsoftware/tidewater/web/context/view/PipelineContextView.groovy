package greenmoonsoftware.tidewater.web.context.view

import greenmoonsoftware.tidewater.config.ContextId

import java.time.Instant

class PipelineContextView {
    String pipelineName
    ContextId contextId
    String script
    Instant startTime
    Instant endTime
}
