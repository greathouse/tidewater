package greenmoonsoftware.tidewater.step
import greenmoonsoftware.tidewater.Context

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
        if (name == 'parameters') {
            return context.parameters
        }
        def executedStep = context.findExecutedStep(name)
        if (!executedStep) {
            println """\
                !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        Unable to find step named \"${name}\".
                !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!""".stripIndent()
            throw new RuntimeException("Unable to find step named \"${name}\"")
        }
        return executedStep
    }

    def methodMissing(String name, args) {
        step[name] = args[0]
    }
}
