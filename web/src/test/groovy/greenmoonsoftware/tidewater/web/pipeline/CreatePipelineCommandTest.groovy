package greenmoonsoftware.tidewater.web.pipeline

import greenmoonsoftware.tidewater.web.pipeline.events.PipelineCreatedEvent
import org.testng.Assert
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class CreatePipelineCommandTest {
    @Test
    void shouldRaisePipelineCreatedEvent() {
        def expectedName = UUID.randomUUID().toString()
        def expectedScript = UUID.randomUUID().toString()

        def actual = new CreatePipelineCommand(expectedName, expectedScript).execute()

        assert actual.size() == 1
        def aEvent = actual[0] as PipelineCreatedEvent
        assert aEvent.aggregateId == expectedName
        assert aEvent.scriptText == expectedScript
    }

    @DataProvider
    Object[][] invalidNames() {[
            [null],
            ['']
    ]}


    @Test(dataProvider = 'invalidNames')
    void givenInvalidName_shouldThrowIllegalArgumentException(String name) {
        try {
            new CreatePipelineCommand(name, UUID.randomUUID().toString()).execute()
            Assert.fail('Should have raised an exception')
        }
        catch (IllegalArgumentException e) {
            assert true
        }
    }
}
