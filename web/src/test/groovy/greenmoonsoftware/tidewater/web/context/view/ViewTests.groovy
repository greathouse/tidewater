package greenmoonsoftware.tidewater.web.context.view
import greenmoonsoftware.tidewater.config.ContextId
import greenmoonsoftware.tidewater.web.pipeline.DatabaseInitializer
import greenmoonsoftware.tidewater.web.context.events.PipelineContextStartedEvent
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import javax.sql.DataSource
import java.time.Instant

class ViewTests {
    private PipelineContextViewEventSubscriber subscriber
    private DataSource dataSource
    private PipelineRunViewQuery view

    @BeforeMethod
    void onSetup() {
        dataSource = DatabaseInitializer.initalize()
        subscriber = new PipelineContextViewEventSubscriber(dataSource)
        view = new PipelineRunViewQuery(dataSource)
        postStartEvent('negative', new ContextId(UUID.randomUUID().toString()), '{negative}', Instant.now())
    }

    @Test
    void givenPipelineRunStartedEvent_shouldBeAbleToQueryForPipelineRunByContextId() {
        String pipelineName = "name-${UUID.randomUUID()}"
        def contextId = new ContextId(UUID.randomUUID().toString())
        def script = '{}'
        def start = Instant.now()
        postStartEvent(pipelineName, contextId, script, start)

        def actual = this.view.getByContextId(contextId)
        assert actual
        assert actual.pipelineName == pipelineName
        assert actual.contextId == contextId
        assert actual.script == script
        assert actual.startTime == start
        assert !actual.endTime
    }

    private postStartEvent(String pipelineName, ContextId contextId, String script, Instant start) {
        this.subscriber.onEvent(new PipelineContextStartedEvent(pipelineName, contextId, script, start))
    }
}
