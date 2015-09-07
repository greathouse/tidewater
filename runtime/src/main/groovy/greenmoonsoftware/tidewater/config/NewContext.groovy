package greenmoonsoftware.tidewater.config
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.es.event.SimpleEventBus
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreEventSubscriber
import greenmoonsoftware.tidewater.config.events.ContextExecutionEndedEvent
import greenmoonsoftware.tidewater.config.events.ContextExecutionStartedEvent
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.events.StepLogEvent
import greenmoonsoftware.tidewater.step.events.StepStartedEvent

final class NewContext implements Context, EventSubscriber<Event> {
    @Delegate private final ContextAttributes attributes
    private Step currentStep
    private final eventBus = new SimpleEventBus()

    NewContext() {
        this(new ContextId(new Date().format('yyyy-MM-dd_HH-mm-ss')))
    }

    NewContext(ContextId id) {
        attributes = new ContextAttributes(id)
        attributes.metaDirectory.mkdirs()
        def storage = new TidewaterEventStoreConfiguration(attributes.metaDirectory)
        addEventSubscribers(
                this,
                new JdbcStoreEventSubscriber(storage.toConfiguration(), storage.datasource),
                new ContextAttributeEventSubscriber(attributes)
        )
    }

    void addEventSubscribers(EventSubscriber<Event>... subscriber) {
        subscriber.each {eventBus.register(it)}
    }

    @Override
    void raiseEvent(Event event) {
        eventBus.post(event)
    }

    @Override
    Step findExecutedStep(String name) {
        attributes.executedSteps[name]
    }

    def execute(String scriptText) {
        attributes.script = scriptText
        new Thread({
            processScript(scriptText)
            raiseEvent(new ContextExecutionStartedEvent(attributes))
            executeSteps()
            raiseEvent(new ContextExecutionEndedEvent(attributes))
        }).start()
    }

    private executeSteps() {
        new StepRunner(this, attributes.definedSteps.values() as List).run()
    }

    private void processScript(String scriptText) {
        TidewaterBaseScript.configure(this, scriptText).run()
    }

    @Override
    void log(String message) {
        log(currentStep, message)
    }

    void log(Step step, String message) {
        raiseEvent(new StepLogEvent(step, message))
    }

    @Override
    void onEvent(Event event) {
        EventApplier.apply(this, event)
    }

    private void handle(StepStartedEvent event) {
        currentStep = event.step
    }
}
