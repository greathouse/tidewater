package greenmoonsoftware.tidewater.web.pipeline

import groovy.transform.Canonical

import java.time.Instant

@Canonical
class Pipeline {
    enum Status {
        UNKNOWN,
        RUNNING,
        SUCCESS,
        FAILURE
    }

    String name
    String script
    Instant lastSuccessfulRun
    Instant lastFailure
    Status status
}
