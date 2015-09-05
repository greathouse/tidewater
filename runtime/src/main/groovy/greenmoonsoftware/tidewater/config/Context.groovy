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
import greenmoonsoftware.tidewater.step.events.*

import java.time.Duration

final class Context {
    @Delegate private final ContextAttributes attributes
    private Step currentStep
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
        new Thread({
            def script = TidewaterBaseScript.configure(this, scriptText)
            script.run()
            raiseEvent(new ContextExecutionStartedEvent(scriptText, attributes))
            for (defined in attributes.definedSteps.values()) {
                def success = start(configure(defined))
                if (!success) {
                    break
                }
            }
            raiseEvent(new ContextExecutionEndedEvent(attributes))
        }).start()
    }

    void log(String message) {
        log(currentStep, message)
    }

    void log(Step step, String message) {
        raiseEvent(new StepLogEvent(step, message))
    }

    private boolean start(Step step) {
        currentStep = step
        def startDate = new Date()
        raiseEvent(new StepStartedEvent(step, startDate))
        try {
            return executeStep(step, startDate)
        } catch (all) {
            handleErroredStep(step, startDate, all)
            return false
        }
    }

    private StepErroredEvent handleErroredStep(Step step, Date startDate, Exception all) {
        def endDate = new Date()
        raiseEvent(new StepErroredEvent(step, endDate, Duration.between(startDate.toInstant(), endDate.toInstant()), all))
    }

    private boolean executeStep(Step step, Date startDate) {
        def success = step.execute(this, setupStepMetaDirectory(step))
        def endDate = new Date()
        if (success) {
            raiseEvent(new StepSuccessfullyCompletedEvent(step, endDate, Duration.between(startDate.toInstant(), endDate.toInstant())))
        } else {
            raiseEvent(new StepFailedEvent(step, endDate, Duration.between(startDate.toInstant(), endDate.toInstant())))
        }
        return success
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
        raiseEvent(new StepConfiguredEvent(step))
        return step
    }


}
