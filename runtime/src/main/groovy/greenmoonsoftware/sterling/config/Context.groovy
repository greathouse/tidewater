package greenmoonsoftware.sterling.config

final class Context {
    private static Context context
    private LinkedHashMap<String, Step> steps = [:]
    private File workspace

    static Context get() {
        if (!context) { context = new Context(workspace: new File("/Users/robert/tmp/sterling/${new Date().format('yyyyMMddHHmmssSSSS')}"))}
        return context
    }

    public File getWorkspace() {
        return workspace
    }

    def execute() {
        println "Workspace: ${workspace}"
        println "Number of steps: ${steps.size()}"
        steps.values().each { it.execute() }
    }

    def step(Definition definition) {
        steps[definition.name] = definition.step
    }

    def methodMissing(String name, args) {
        def step = args[0].type.newInstance()
        step.with args[1]
        return new Definition(name:name, step:step)
    }

    private class Definition {
        String name
        Step step
    }
}
