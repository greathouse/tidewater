package greenmoonsoftware.tidewater.web.pipeline.commands

import greenmoonsoftware.tidewater.web.pipeline.events.PipelineStartedEvent
import org.testng.annotations.Test

import java.time.Instant

class StartPipelineCommandTests {
    @Test
    void shouldRaisePipelineStartedEvent() {
        def expectedName = UUID.randomUUID().toString()
        def expectedStart = Instant.now()

        def actual = new StartPipelineCommand(expectedName, expectedStart).execute()

        assert actual.size() == 1
        def aEvent = actual[0] as PipelineStartedEvent
        assert aEvent.aggregateId == expectedName
        assert aEvent.start == expectedStart
    }
}
