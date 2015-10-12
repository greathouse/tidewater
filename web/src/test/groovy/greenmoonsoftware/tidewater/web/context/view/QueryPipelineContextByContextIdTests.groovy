package greenmoonsoftware.tidewater.web.context.view
import greenmoonsoftware.tidewater.context.ContextId
import greenmoonsoftware.tidewater.web.context.PipelineContextStatus
import greenmoonsoftware.tidewater.web.context.events.PipelineContextEndedEvent
import org.testng.annotations.Test

import java.time.Instant

class QueryPipelineContextByContextIdTests extends QueryPipelineContextTestBase {
    private QueryPipelineContextByContextId view

    protected void onSetup() {
        view = new QueryPipelineContextByContextId(dataSource)
        startContext('negative', new ContextId(UUID.randomUUID().toString()), '{negative}', Instant.now())
    }

    @Test
    void givenPipelineContextStartedEvent_shouldBeAbleToQueryForPipelineRunByContextId() {
        String pipelineName = "name-${UUID.randomUUID()}"
        def contextId = new ContextId(UUID.randomUUID().toString())
        def script = '{}'
        def start = Instant.now()
        startContext(pipelineName, contextId, script, start)

        def actual = this.view.getByContextId(contextId)
        assert actual
        assert actual.pipelineName == pipelineName
        assert actual.contextId == contextId
        assert actual.startTime == start
        assert actual.status == PipelineContextStatus.IN_PROGRESS
        assert actual.endTime == Instant.EPOCH
    }

    @Test
    void givenPipelineContextEndedEvent_shouldUpdateEndTimeOnAggregate() {
        def contextId = new ContextId(UUID.randomUUID().toString())
        startContext(data(), contextId, data(), Instant.now())

        def end = Instant.now().plusSeconds(100)
        def status = PipelineContextStatus.COMPLETE
        subscriber.onEvent(new PipelineContextEndedEvent(contextId, end, status))

        def actual = view.getByContextId(contextId)
        assert actual.endTime == end
        assert actual.status == status
    }

    private String data() {
        UUID.randomUUID().toString()
    }
}
