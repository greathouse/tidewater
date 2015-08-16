package greenmoonsoftware.tidewater.config

class StepDelegate {
    private Step step

    StepDelegate(Step s) {
        step = s
    }

    def propertyMissing(String name) {
        if (name == 'context') {
            return Context.get()
        }
        def executedStep = Context.get().findExecutedStep(name)
        if (!executedStep) {
            println """\
                !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        Unable to find step named \"${name}\".
                !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!""".stripIndent()
            System.exit(1)
        }
        return executedStep
    }

    def methodMissing(String name, args) {
        step[name] = args[0]
    }
}
