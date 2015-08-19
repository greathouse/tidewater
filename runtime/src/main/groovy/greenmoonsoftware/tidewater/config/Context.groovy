package greenmoonsoftware.tidewater.config

import greenmoonsoftware.tidewater.config.step.CustomStep
import greenmoonsoftware.tidewater.config.step.Step
import greenmoonsoftware.tidewater.config.step.StepConfiguration
import greenmoonsoftware.tidewater.config.step.StepDelegate
import groovy.time.TimeCategory

final class Context {
    private static Context context
    private LinkedHashMap<String, StepConfiguration> configuredSteps = [:]
    private LinkedHashMap<String, Step> executedSteps = [:]
    private File workspace
    private File metaDirectory

    static Context get() {
        if (!context) {
            context = new Context(
                    workspace: new File("/Users/robert/tmp/tidewater/${new Date().format('yyyyMMddHHmmssSSSS')}")
            )
            .initialize()
        }
        return context
    }

    private Context initialize() {
        workspace.mkdirs()
        metaDirectory = new File(workspace, '.meta')
        metaDirectory.mkdirs()
        return this
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
        println "Number of steps: ${configuredSteps.size()}"

        configuredSteps.values().each { configured ->
            def step = configure(configured)
            printBanner(configured, step)
            executeStep(step, configured)
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
        step.execute(System.out, stepDirectory)
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

    def step(StepConfiguration definition) {
        configuredSteps[definition.name] = definition
    }

    def methodMissing(String name, args) {
        if (args.length == 1) {
            return customStep(name, args)
        }
        return stepConfiguration(name, args)
    }

    StepConfiguration stepConfiguration(String name, args) {
        def type = args[0].type
        def configureClosure = args[-1]
        return new StepConfiguration(name:name, type: type, configureClosure: configureClosure)
    }

    StepConfiguration customStep(String name, args) {
        Closure c = args[0]
        return new StepConfiguration(name: name, type: CustomStep, configureClosure: { executable c})
    }
}
