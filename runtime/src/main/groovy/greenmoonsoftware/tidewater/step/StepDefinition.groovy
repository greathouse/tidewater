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
            return new StepDefinition(name: name, type: type, enabled: enabled, configureClosure: configureClosure)
        }

        Builder scriptArgs(List args) {
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
