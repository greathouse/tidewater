package greenmoonsoftware.tidewater.web.pipeline.runs.view

import greenmoonsoftware.tidewater.config.ContextId

import java.time.Instant

class PipelineRunView {
    String pipelineName
    ContextId contextId
    String script
    Instant startTime
    Instant endTime
}
