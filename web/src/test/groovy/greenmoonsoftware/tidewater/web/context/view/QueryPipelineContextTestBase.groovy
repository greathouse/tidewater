package greenmoonsoftware.tidewater.web.context.view

import greenmoonsoftware.tidewater.context.ContextId
import greenmoonsoftware.tidewater.web.context.events.PipelineContextStartedEvent
import greenmoonsoftware.tidewater.web.pipeline.DatabaseInitializer
import org.testng.annotations.BeforeMethod

import javax.sql.DataSource
import java.time.Instant

abstract class QueryPipelineContextTestBase {
    protected PipelineContextViewEventSubscriber subscriber
    protected DataSource dataSource

    @BeforeMethod
    final void setup() {
        dataSource = DatabaseInitializer.initalize()
        subscriber = new PipelineContextViewEventSubscriber(dataSource)
        onSetup()
    }

    protected void onSetup(){}

    protected startContext(String pipelineName, ContextId contextId, String script, Instant start) {
        this.subscriber.onEvent(new PipelineContextStartedEvent(pipelineName, contextId, script, start))
    }
}
