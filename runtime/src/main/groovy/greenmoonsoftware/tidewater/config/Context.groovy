package greenmoonsoftware.tidewater.config
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.es.event.SimpleEventBus
import greenmoonsoftware.tidewater.config.step.*
import groovy.time.TimeCategory

final class Context implements EventSubscriber<Event> {
    private static ThreadLocal<Context> contextThreadLocal = new ThreadLocal<>()

    private definedSteps = [:] as LinkedHashMap<String, StepConfiguration>
    private executedSteps = [:] as LinkedHashMap<String, Step>
    private File workspace = new File("${Tidewater.WORKSPACE_ROOT}/${new Date().format('yyyyMMddHHmmssSSSS')}")
    private File metaDirectory
    private def eventBus = new SimpleEventBus()

    @Deprecated
    static Context get() {
        return contextThreadLocal.get() ?: new Context()
    }

    Context() {
        workspace = new File("${Tidewater.WORKSPACE_ROOT}/${new Date().format('yyyyMMddHHmmssSSSS')}")
        metaDirectory = new File(workspace, '.meta')
        workspace.mkdirs()
        metaDirectory.mkdirs()

        contextThreadLocal.set(this)
        addEventSubscribers(this)
    }

    void addEventSubscribers(EventSubscriber<Event>... subscriber) {
        subscriber.each {eventBus.register(it)}
    }

    void removeEventSubscriber(EventSubscriber<Event> sub) {
        eventBus.unregister(sub)
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

    def findExecutedStep(String name) {
        executedSteps[name]
    }

    def execute() {
        println "Workspace: ${workspace}"
        println "Number of steps: ${definedSteps.size()}"

        definedSteps.values().each { defined ->
            def step = configure(defined)
            executeStep(step)
        }
    }

    private void executeStep(Step step) {
        def stepDirectory = setupStepMetaDirectory(step)
        def serializer = serializer(stepDirectory)

        def startTime = new Date()
        raiseEvent(new StepStartedEvent(step, startTime))

        step.execute(this, stepDirectory)
        def endTime = new Date()
        raiseEvent(new StepSuccessEvent(step, endTime, TimeCategory.minus(endTime, startTime)))
        removeEventSubscriber(serializer)
    }

    private StepSerializerSubscriber serializer(File stepDirectory) {
        def serializer = new StepSerializerSubscriber(stepDirectory)
        addEventSubscribers(serializer)
        serializer
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
        c.delegate = new StepDelegate(step)
        c.resolveStrategy = Closure.DELEGATE_FIRST
        c.call()
        step
    }

    @Override
    void onEvent(Event event) {
        EventApplier.apply(this, event)
    }

    private void handle(StepSuccessEvent event) {
        executedSteps[event.step.name] = event.step
    }

    private void handle(StepConfiguredEvent event) {
        def stepDef = event.definition
        definedSteps[stepDef.name] = stepDef
    }
}
