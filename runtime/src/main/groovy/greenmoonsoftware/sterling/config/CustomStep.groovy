package greenmoonsoftware.sterling.config

class CustomStep implements Step {
    Closure executable

    @Override
    void execute(PrintStream log, File metaDirectory) {
        def c = (Closure) executable.clone()
        c.delegate = new StepDelegate(this)
        c.call()
    }

    @Override
    Map<String, Object> getInputs() {
        [:].asImmutable()
    }

    @Override
    Map<String, Object> getOutputs() {
        [:].asImmutable()
    }
}
