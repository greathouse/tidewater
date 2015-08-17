package greenmoonsoftware.tidewater.config.step

class CustomStep extends AbstractStep {
    Closure executable

    @Override
    void execute(PrintStream log, File metaDirectory) {
        def c = (Closure) executable.clone()
        c.delegate = new StepDelegate(this)
        c.call()
    }
}
