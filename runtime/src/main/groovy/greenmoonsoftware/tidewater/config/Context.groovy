package greenmoonsoftware.tidewater.config

import greenmoonsoftware.tidewater.config.step.Step
import greenmoonsoftware.tidewater.config.step.StepConfiguration
import greenmoonsoftware.tidewater.config.step.StepDelegate
import groovy.time.TimeCategory

final class Context {
    private static ThreadLocal<Context> contextThreadLocal = new ThreadLocal<>()

    @Deprecated private static Context context
    private LinkedHashMap<String, StepConfiguration> definedSteps = [:]
    private LinkedHashMap<String, Step> executedSteps = [:]
    private File workspace = new File("${Tidewater.WORKSPACE_ROOT}/${new Date().format('yyyyMMddHHmmssSSSS')}")
    private File metaDirectory

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
            printBanner(defined, step)
            executeStep(step, defined)
            printFooter(step)
        }
    }

    private void printFooter(Step step) {
        step.outputs.each { println "\t${it.key}: ${it.value}" }
        println ''
    }

    private void executeStep(Step step, StepConfiguration configured) {
        def stepDirectory = setupStepMetaDirectory(configured)
        def stepFile = new File(stepDirectory, 'step.json')
        step.seralize(stepFile)

        def startTime = new Date()
        step.execute(new LogWriter(new File(stepDirectory, 'log.txt')), stepDirectory)
        def endTime = new Date()
        println "\n${configured.name} completed. Took ${TimeCategory.minus(endTime, startTime)}"
        step.seralize(stepFile)
        executedSteps[configured.name] = step
    }

    private File setupStepMetaDirectory(StepConfiguration configured) {
        def stepDirectory = new File(metaDirectory, configured.name)
        stepDirectory.mkdirs()
        return stepDirectory
    }

    private void printBanner(StepConfiguration configured, Step step) {
        println '~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~'
        println "${configured.name} (${configured.type.simpleName})"
        step.inputs.each { println "\t${it.key}: ${it.value}" }
        println '~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~'
    }

    private Step configure(StepConfiguration configured) {
        def step = configured.type.newInstance()
        def c = (Closure) configured.configureClosure.clone()
        c.delegate = new StepDelegate(step)
        c.resolveStrategy = Closure.DELEGATE_FIRST
        c.call()
        step
    }
}
