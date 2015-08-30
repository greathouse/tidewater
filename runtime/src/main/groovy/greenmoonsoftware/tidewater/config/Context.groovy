package greenmoonsoftware.tidewater.config
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.es.event.SimpleEventBus
import greenmoonsoftware.tidewater.config.step.*
import groovy.time.TimeCategory
import groovy.time.TimeDuration

final class Context implements EventSubscriber<Event> {
    private static ThreadLocal<Context> contextThreadLocal = new ThreadLocal<>()

    @Deprecated private static Context context
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
        eventBus.register(this)
    }

    void raiseEvent(Event event) {
        eventBus.post(event)
    }

    void addStep(StepConfiguration stepDef) {
        definedSteps[stepDef.name] = stepDef
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

    private void printFooter(Step step, TimeDuration duration) {
        println "\n${step.name} completed. Took $duration"
        step.outputs.each { println "\t${it.key}: ${it.value}" }
        println ''
    }

    private void executeStep(Step step) {
        def stepDirectory = setupStepMetaDirectory(step)
        def stepFile = new File(stepDirectory, 'step.json')
        step.seralize(stepFile)

        def startTime = new Date()
        raiseEvent(new StepStartedEvent(step))
        step.execute(new LogWriter(new File(stepDirectory, 'log.txt')), stepDirectory)
        def endTime = new Date()
        raiseEvent(new StepSuccessEvent(step, TimeCategory.minus(endTime, startTime)))
        step.seralize(stepFile)
        executedSteps[step.name] = step
    }

    private File setupStepMetaDirectory(Step step) {
        def stepDirectory = new File(metaDirectory, step.name)
        stepDirectory.mkdirs()
        return stepDirectory
    }

    private void printBanner(Step step) {
        println '~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~'
        println "${step.name} (${step.class.simpleName})"
        step.inputs.each { println "\t${it.key}: ${it.value}" }
        println '~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~'
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
        EventApplier.apply(this, event);
    }

    private void handle(StepStartedEvent event) {
        printBanner(event.step)
    }

    private void handle(StepSuccessEvent event) {
        printFooter(event.step, event.duration)
    }
}
