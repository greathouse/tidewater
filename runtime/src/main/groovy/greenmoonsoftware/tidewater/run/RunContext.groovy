package greenmoonsoftware.tidewater.run
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.es.event.SimpleEventBus
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreEventSubscriber
import greenmoonsoftware.tidewater.context.AbstractContext
import greenmoonsoftware.tidewater.context.Context
import greenmoonsoftware.tidewater.context.ContextAttributeEventSubscriber
import greenmoonsoftware.tidewater.context.ContextAttributes
import greenmoonsoftware.tidewater.context.ContextId
import greenmoonsoftware.tidewater.context.StepRunner
import greenmoonsoftware.tidewater.context.TidewaterBaseScript
import greenmoonsoftware.tidewater.context.TidewaterEventStoreConfiguration
import greenmoonsoftware.tidewater.context.events.ContextExecutionEndedEvent
import greenmoonsoftware.tidewater.context.events.ContextExecutionStartedEvent
import greenmoonsoftware.tidewater.json.JsonEventSerializer
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDefinition
import greenmoonsoftware.tidewater.step.events.StepDefinedEvent
import greenmoonsoftware.tidewater.step.events.StepLogEvent
import greenmoonsoftware.tidewater.step.events.StepStartedEvent

final class RunContext extends AbstractContext implements Context, EventSubscriber<Event> {
    final ContextAttributes attributes
    private Step currentStep
    private final eventBus = new SimpleEventBus()

    RunContext() {
        this(new ContextId(new Date().format('yyyy-MM-dd_HH-mm-ss')))
    }

    RunContext(ContextId id) {
        attributes = new ContextAttributes(id)
        attributes.metaDirectory.mkdirs()
        def storage = new TidewaterEventStoreConfiguration(attributes.metaDirectory)
        addEventSubscribers(
                this,
                new JdbcStoreEventSubscriber(storage.toConfiguration(), storage.datasource, new JsonEventSerializer()),
                new ContextAttributeEventSubscriber(this, attributes)
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
    File getWorkspace() {
        attributes.workspace
    }

    @Override
    File getMetaDirectory() {
        attributes.metaDirectory
    }

    @Override
    void addDefinedStep(StepDefinition definition) {
        super.addDefinedStep(definition)
        raiseEvent(new StepDefinedEvent(definition, attributes.id))
    }

    def execute(String scriptText) {
        attributes.script = scriptText
        new Thread({
            processScript(scriptText)
            raiseEvent(new ContextExecutionStartedEvent(attributes))
            executeSteps()
            raiseEvent(new ContextExecutionEndedEvent(attributes))
        }, "ContextRunThread-${attributes.id}").start()
    }

    private executeSteps() {
        new StepRunner(this, definedSteps.values() as List).run()
    }

    private void processScript(String scriptText) {
        TidewaterBaseScript.configure(this, scriptText).run()
    }

    @Override
    void log(String message) {
        log(currentStep, message)
    }

    void log(Step step, String message) {
        raiseEvent(new StepLogEvent(step, attributes.id, message))
    }

    @Override
    void onEvent(Event event) {
        EventApplier.apply(this, event)
    }

    private void handle(StepStartedEvent event) {
        currentStep = event.step
    }
}
