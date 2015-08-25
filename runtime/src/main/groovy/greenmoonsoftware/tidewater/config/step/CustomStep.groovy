package greenmoonsoftware.tidewater.config.step

import greenmoonsoftware.tidewater.config.LogWriter

class CustomStep extends AbstractStep {
    Closure executable

    @Override
    void execute(LogWriter log, File metaDirectory) {
        def c = (Closure) executable.clone()
        c.delegate = new StepDelegate(this)
        c.call()
    }
}
