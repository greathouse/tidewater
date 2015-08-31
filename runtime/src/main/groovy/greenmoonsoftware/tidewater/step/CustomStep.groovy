package greenmoonsoftware.tidewater.step

import greenmoonsoftware.tidewater.config.Context

class CustomStep extends AbstractStep {
    transient Closure executable

    @Override
    void execute(Context context, File metaDirectory) {
        def c = (Closure) executable.clone()
        c.delegate = new StepDelegate(context, this)
        c.call()
    }
}
