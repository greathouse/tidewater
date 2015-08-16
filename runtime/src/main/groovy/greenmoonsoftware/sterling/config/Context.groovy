package greenmoonsoftware.sterling.config

import groovy.time.TimeCategory

final class Context {
    private static Context context
    private LinkedHashMap<String, StepConfiguration> configuredSteps = [:]
    private LinkedHashMap<String, Step> executedSteps = [:]
    private File workspace

    static Context get() {
        if (!context) { context = new Context(workspace: new File("/Users/robert/tmp/sterling/${new Date().format('yyyyMMddHHmmssSSSS')}"))}
        return context
    }

    public File getWorkspace() {
        return workspace
    }

    def findExecutedStep(String name) {
        executedSteps[name]
    }

    def execute() {
        println "Workspace: ${workspace}"
        println "Number of steps: ${configuredSteps.size()}"

        configuredSteps.values().each { configured ->
            def step = configured.type.newInstance()
            def c = (Closure)configured.configureClosure.clone()
            c.delegate = new StepDelegate(step)
            c.resolveStrategy = Closure.DELEGATE_FIRST
            c.call()

            println '~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~'
            println "${configured.name} (${configured.type.simpleName})"
            step.inputs.each { println "\t${it.key}: ${it.value}"}
            println '~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~'
            def startTime = new Date()
            step.execute(System.out)
            def endTime = new Date()
            println "\n${configured.name} completed. Took ${TimeCategory.minus(endTime, startTime)}"
            step.outputs.each { println "\t${it.key}: ${it.value}" }
            println ''

            executedSteps[configured.name] = step
        }
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
