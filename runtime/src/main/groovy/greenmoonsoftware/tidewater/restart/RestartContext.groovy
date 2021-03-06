package greenmoonsoftware.tidewater.restart
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.es.event.SimpleEventBus
import greenmoonsoftware.es.event.jdbcstore.JdbcEventQuery
import greenmoonsoftware.es.event.jdbcstore.JdbcStoreEventSubscriber
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.context.*
import greenmoonsoftware.tidewater.context.events.ContextExecutionEndedEvent
import greenmoonsoftware.tidewater.json.JsonEventSerializer
import greenmoonsoftware.tidewater.restart.events.ContextExecutionRestartedEvent
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDefinition
import greenmoonsoftware.tidewater.step.events.StepLogEvent
import greenmoonsoftware.tidewater.step.events.StepStartedEvent

class RestartContext extends AbstractContext implements EventSubscriber<Event>, Context {
    final ContextAttributes attributes
    private final JdbcEventQuery eventQuery
    private final eventBus = new SimpleEventBus()
    private final List<EventSubscriber<Event>> subscribers = []
    private Step currentStep

    RestartContext(ContextId id) {
        attributes = new ContextAttributes(id)
        def storage = new TidewaterEventStoreConfiguration(attributes.metaDirectory)
        addEventSubscribers(new JdbcStoreEventSubscriber(storage.toConfiguration(), storage.datasource, new JsonEventSerializer()))
        eventQuery = new JdbcEventQuery(storage.toConfiguration(), storage.datasource, new JsonEventSerializer())
    }

    void restart() {
        eventBus.register(new ContextAttributeEventSubscriber(this, attributes))
        eventBus.register(this)
        replayToPreviousState()
        registerExternalEventSubscribers()
        new Thread({
            raiseEvent(new ContextExecutionRestartedEvent(attributes.id))
            processScript()
            executeSteps()
            raiseEvent(new ContextExecutionEndedEvent(attributes))
        }).start()
    }

    private executeSteps() {
        new StepRunner(this, determineRemainingSteps()).run()
    }

    private void processScript() {
        def script = TidewaterBaseScript.configure(this, attributes.script)
        script.run()
    }

    private List<StepDefinition> determineRemainingSteps() {
        def definedSteps = definedSteps.values() as List
        def remainingSteps = definedSteps[definedSteps.findIndexOf {
            it.name == currentStep.name
        }..<definedSteps.size()]
        remainingSteps
    }

    private List<EventSubscriber<Event>> registerExternalEventSubscribers() {
        subscribers.each { eventBus.register(it) }
    }

    private List<Event> replayToPreviousState() {
        eventQuery.events.each { event ->
            eventBus.post(event)
        }
    }

    RestartContext addEventSubscribers(EventSubscriber<Event>... subscriber) {
        subscribers.addAll(subscriber)
        return this
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
    void onEvent(Event event) {
        EventApplier.apply(this, event)
    }

    private void handle(StepStartedEvent event) {
        currentStep = event.step
    }

    @Override
    void raiseEvent(Event event) {
        eventBus.post(event)
    }

    @Override
    void log(Step s, String logMessageId, String message) {
        def event = new StepLogEvent(s, attributes.id, message)
        event.logMessageId = logMessageId
        raiseEvent(event)
    }

    @Override
    void log(Step step, String message) {
        raiseEvent(new StepLogEvent(step, attributes.id, message))
    }

    @Override
    void log(String message) {
        log(currentStep, message)
    }
}
