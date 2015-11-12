package greenmoonsoftware.tidewater.context

import greenmoonsoftware.es.event.Event
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDefinition
import org.testng.Assert
import org.testng.annotations.Test

class AbstractContextTests {
    @Test
    void givenStepDefinedTwice_shouldThrowException() {
        def context = new TestContext()

        def definition = new StepDefinition(name: 'clone')
        context.addDefinedStep(definition)
        try {
            context.addDefinedStep(definition)
            Assert.fail('Should have raised an exception')
        }
        catch (InvalidScriptException e) {
            assert true
        }
    }

    private class TestContext extends AbstractContext {

        @Override
        ContextAttributes getAttributes() {
            return null
        }

        @Override
        File getWorkspace() {
            return null
        }

        @Override
        File getMetaDirectory() {
            return null
        }

        @Override
        void raiseEvent(Event event) {

        }

        @Override
        void log(Step s, String message) {

        }

        @Override
        void log(String message) {

        }
    }
}
