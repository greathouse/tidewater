package greenmoonsoftware.tidewater.config
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.es.event.SimpleEventBus
import greenmoonsoftware.tidewater.config.step.*
import groovy.time.TimeCategory

final class Context implements EventSubscriber<Event> {
    private final definedSteps = [:] as LinkedHashMap<String, StepConfiguration>
    private final executedSteps = [:] as LinkedHashMap<String, Step>
    private final workspace = new File("${Tidewater.WORKSPACE_ROOT}/${new Date().format('yyyyMMddHHmmssSSSS')}")
    private final metaDirectory = new File(workspace, '.meta')
    private final eventBus = new SimpleEventBus()

    Context() {
        metaDirectory.mkdirs()
        addEventSubscribers(this)
    }

    void addEventSubscribers(EventSubscriber<Event>... subscriber) {
        subscriber.each {eventBus.register(it)}
    }

    void raiseEvent(Event event) {
        eventBus.post(event)
    }

    File getWorkspace() {
        workspace
    }

    File getMetaDirectory() {
        metaDirectory
    }

    Map getDefinedSteps() {
        definedSteps.asImmutable()
    }

    def findExecutedStep(String name) {
        executedSteps[name]
    }

    def execute() {
        raiseEvent(new ContextExecutionStartedEvent(this))
        definedSteps.values().each { defined ->
            executeStep(configure(defined))
        }
    }

    private void executeStep(Step step) {
        def startTime = new Date()
        raiseEvent(new StepStartedEvent(step, startTime))
        step.execute(this, setupStepMetaDirectory(step))
        def endTime = new Date()
        raiseEvent(new StepSuccessfullyCompletedEvent(step, endTime, TimeCategory.minus(endTime, startTime)))
    }

    private File setupStepMetaDirectory(Step step) {
        def stepDirectory = new File(metaDirectory, step.name)
        stepDirectory.mkdirs()
        return stepDirectory
    }

    private Step configure(StepConfiguration configured) {
        def step = configured.type.newInstance()
        step.name = configured.name
        def c = (Closure) configured.configureClosure.clone()
        c.delegate = new StepDelegate(this, step as Step)
        c.resolveStrategy = Closure.DELEGATE_ONLY
        c.call()
        step
    }

    @Override
    void onEvent(Event event) {
        EventApplier.apply(this, event)
    }

    private void handle(StepSuccessfullyCompletedEvent event) {
        executedSteps[event.step.name] = event.step
    }

    private void handle(StepConfiguredEvent event) {
        def stepDef = event.definition
        definedSteps[stepDef.name] = stepDef
    }
}
