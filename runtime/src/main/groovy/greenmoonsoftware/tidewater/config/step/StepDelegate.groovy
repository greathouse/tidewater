package greenmoonsoftware.tidewater.config.step

import greenmoonsoftware.tidewater.config.Context

final class StepDelegate {
    private Context context
    private Step step

    StepDelegate(Context c, Step s) {
        context = c
        step = s
    }

    def propertyMissing(String name) {
        if (name == 'context') {
            return context
        }
        def executedStep = context.findExecutedStep(name)
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
