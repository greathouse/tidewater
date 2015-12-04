package greenmoonsoftware.tidewater.step

class StepDefinition  {
    String name
    String type
    boolean enabled = true
    transient Closure configureClosure

    static Builder builder() {
        new Builder()
    }

    static class Builder {
        private String name
        private Map definitionArgs
        private Closure configureClosure

        StepDefinition build() {
            def type = definitionArgs?.type ?: CustomStep.canonicalName
            def enabled = definitionArgs?.enabled == null ? true : definitionArgs.enabled
            return new StepDefinition(name: name, type: type, enabled: enabled, configureClosure: wrapConfigureClosureIfNecessary(type))
        }

        private wrapConfigureClosureIfNecessary(String type) {
            //Ensure the CustomStep closure doesn't execute during the "configuration" phase.
            //The 'executable' property in the wrapped closure maps to the greenmoonsoftware.tidewater.step.CustomStep::executable property
            def c = configureClosure
            return type == CustomStep.canonicalName ? { executable c} : configureClosure
        }

        Builder scriptArgs(args) {
            definitionArgs = args.size() == 2 ? args[0] : [:]
            configureClosure = args.size() == 2 ? args[1] : args[0]
            return this
        }

        Builder name(String n) {
            name = n
            return this
        }
    }
}
