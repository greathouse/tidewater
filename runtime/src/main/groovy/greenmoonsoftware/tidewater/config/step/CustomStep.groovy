package greenmoonsoftware.tidewater.config.step

import greenmoonsoftware.tidewater.config.Context

class CustomStep extends AbstractStep {
    Closure executable

    @Override
    void execute(Context context, File metaDirectory) {
        def c = (Closure) executable.clone()
        c.delegate = new StepDelegate(this)
        c.call()
    }
}
