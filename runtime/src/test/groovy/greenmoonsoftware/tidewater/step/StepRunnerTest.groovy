package greenmoonsoftware.tidewater.step
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.context.ContextAttributes
import greenmoonsoftware.tidewater.context.ContextId
import greenmoonsoftware.tidewater.context.StepRunner
import greenmoonsoftware.tidewater.step.events.StepConfiguredEvent
import greenmoonsoftware.tidewater.step.events.StepDisabledEvent
import greenmoonsoftware.tidewater.step.events.StepErroredEvent
import greenmoonsoftware.tidewater.step.events.StepFailedEvent
import greenmoonsoftware.tidewater.step.events.StepStartedEvent
import greenmoonsoftware.tidewater.step.events.StepSuccessfullyCompletedEvent
import org.testng.annotations.Test

import java.nio.file.Files

class StepRunnerTest {
    @Test
    void givenSuccessfulStep_shouldRaiseExpectedEvents() {
        def context = new TestContext()
        def definition = custom {}
        def steps = [definition]
        assert new StepRunner(context, steps).run() == steps.size()
        context.assertEvents(definition.name, successEvents())
    }

    @Test
    void givenErrorStep_shouldRaiseExpectedEvents() {
        def context = new TestContext()
        def steps = [custom { throw new RuntimeException() }]

        assert new StepRunner(context, steps).run() == 0
        context.assertEvents(steps[0].name, errorEvents())
    }

    @Test
    void givenFailureStep_shouldRaiseExpectedEvents() {
        def context = new TestContext()
        def steps = [custom { false }]

        assert new StepRunner(context, steps).run() == 0
        context.assertEvents(steps[0].name, failEvents())
    }

    @Test
    void givenSuccessThenFail_shouldExecuteSuccessStep() {
        def context = new TestContext()
        def steps = [custom { true }, custom { false }]

        assert new StepRunner(context, steps).run() == 1
        context.assertEvents(steps[0].name, successEvents())
        context.assertEvents(steps[1].name, failEvents())
    }

    @Test
    void givenDisabledThenSuccess_shouldExecuteSuccessStep() {
        def context = new TestContext()
        def steps = [custom([enabled: false]) {}, custom {true}]

        assert new StepRunner(context, steps).run() == 2
        context.assertEvents(steps[0].name, disabledEvents())
        context.assertEvents(steps[1].name, successEvents())
    }

    private successEvents() {
        [StepConfiguredEvent, StepStartedEvent, StepSuccessfullyCompletedEvent] as Class<Event>[]
    }

    private failEvents() {
        [StepConfiguredEvent, StepStartedEvent, StepFailedEvent] as Class<Event>[]
    }

    private errorEvents() {
        [StepConfiguredEvent, StepStartedEvent, StepErroredEvent] as Class<Event>[]
    }

    Class<Event>[] disabledEvents() {
        [StepDisabledEvent] as Class<Event>[]
    }

    private custom(Map args, Closure configureClosure) {
        StepDefinition.builder().name(UUID.randomUUID().toString()).scriptArgs([args, configureClosure]).build()
    }

    private custom(Closure configureClosure) {
        custom([enabled: true], configureClosure)
    }

    private static class TestContext implements Context {
        List<Event> raisedEvents = []
        ContextAttributes attributes = new ContextAttributes(new ContextId(UUID.randomUUID().toString()))
        File metaDirectory = Files.createTempDirectory(UUID.randomUUID().toString()).toFile()

        boolean assertEvents(String aggregateId, Class<Event> ... events) {
            def eventsForAggregateId = raisedEvents.findAll { e -> e.aggregateId == aggregateId }
            assert eventsForAggregateId.size() == events.size(), "Expected Events and raised events don't match: Raised Events: ${eventsForAggregateId.collect{it.type}}"
            events.each { eventClass ->
                assert raisedEvents.find { e -> e.type == eventClass.canonicalName && e.aggregateId == aggregateId }, "No event found of type ${eventClass}"
            }
        }

        @Override
        String getParameter(String name) {
            return null
        }

        @Override
        void setParameter(String name, String value) {

        }

        @Override
        Map<String, String> getParameters() {
            return null
        }

        @Override
        def getExt(String name) {
            return null
        }

        @Override
        void setExt(String name, Object value) {

        }

        @Override
        File getWorkspace() {
            return null
        }

        @Override
        void addDefinedStep(StepDefinition definition) {

        }

        @Override
        void addExecutedStep(Step step) {

        }

        @Override
        Step findExecutedStep(String name) {
            return null
        }

        @Override
        void raiseEvent(Event event) {
            raisedEvents << event
        }

        @Override
        void log(Step s, String logMessageId, String message) {

        }

        @Override
        void log(Step s, String message) {

        }

        @Override
        void log(String message) {

        }
    }
}
