package greenmoonsoftware.tidewater.step

import greenmoonsoftware.tidewater.Context

class CustomStep extends AbstractStep {
    transient Closure executable

    @Override
    boolean execute(Context context, File metaDirectory) {
        def c = (Closure) executable.clone()
        c.delegate = new StepDelegate(context, this)
        def success = c.call()
        return (success == null || success)
    }
}
