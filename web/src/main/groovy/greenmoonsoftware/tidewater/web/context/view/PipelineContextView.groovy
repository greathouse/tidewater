package greenmoonsoftware.tidewater.web.context.view

import greenmoonsoftware.tidewater.config.ContextId

import java.time.Instant

class PipelineContextView {
    enum Status {
        IN_PROGRESS(0), ERROR(1), FAILURE(2), COMPLETE(3), ABORT(4), PAUSE(5)

        private static final fromValue = values().collectEntries{[ (it.value) : it]}
        static Status fromValue(int value) { fromValue[value] }

        public final int value
        private Status(int value) { this.value = value }
    }

    String pipelineName
    ContextId contextId
    Status status
    Instant startTime
    Instant endTime
}
