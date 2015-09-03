package greenmoonsoftware.tidewater.config
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.es.event.SimpleEventBus
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreEventSubscriber
import greenmoonsoftware.tidewater.config.events.ContextExecutionEndedEvent
import greenmoonsoftware.tidewater.config.events.ContextExecutionStartedEvent
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepConfiguration
import greenmoonsoftware.tidewater.step.StepDelegate
import greenmoonsoftware.tidewater.step.events.StepStartedEvent
import greenmoonsoftware.tidewater.step.events.StepSuccessfullyCompletedEvent

import java.time.Duration

final class Context {
    @Delegate private final ContextAttributes attributes
    private final eventBus = new SimpleEventBus()

    Context() {
        this(new ContextId(new Date().format('yyyy-MM-dd_HH-mm-ss')))
    }

    Context(ContextId id) {
        attributes = new ContextAttributes(id)
        attributes.metaDirectory.mkdirs()
        def storage = new TidewaterEventStoreConfiguration(attributes.metaDirectory)
        addEventSubscribers(
                new JdbcStoreEventSubscriber(storage.toConfiguration(), storage.datasource),
                new ContextAttributeEventSubscriber(attributes)
        )
    }

    void addEventSubscribers(EventSubscriber<Event>... subscriber) {
        subscriber.each {eventBus.register(it)}
    }

    void raiseEvent(Event event) {
        eventBus.post(event)
    }

    def findExecutedStep(String name) {
        attributes.executedSteps[name]
    }

    def execute(String scriptText) {
        def script = TidewaterBaseScript.configure(this, scriptText)
        script.run()
        raiseEvent(new ContextExecutionStartedEvent(scriptText, attributes))
        attributes.definedSteps.values().each { defined ->
            executeStep(configure(defined))
        }
        raiseEvent(new ContextExecutionEndedEvent(attributes))
    }

    private void executeStep(Step step) {
        def startDate = new Date()
        raiseEvent(new StepStartedEvent(step, startDate))
        step.execute(this, setupStepMetaDirectory(step))
        def endDate = new Date()
        raiseEvent(new StepSuccessfullyCompletedEvent(step, endDate, Duration.between(startDate.toInstant(), endDate.toInstant())))
    }

    private File setupStepMetaDirectory(Step step) {
        def stepDirectory = new File(attributes.metaDirectory, step.name)
        stepDirectory.mkdirs()
        return stepDirectory
    }

    private Step configure(StepConfiguration configured) {
        def step = configured.type.newInstance() as Step
        step.name = configured.name
        def c = (Closure) configured.configureClosure.rehydrate(new StepDelegate(this, step), this, this)
        c.resolveStrategy = Closure.DELEGATE_ONLY
        c.call()
        return step
    }


}
