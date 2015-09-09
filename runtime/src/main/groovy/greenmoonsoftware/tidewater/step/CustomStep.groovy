package greenmoonsoftware.tidewater.step

import greenmoonsoftware.tidewater.config.Context

class CustomStep extends AbstractStep {
    transient Closure executable

    @Override
    boolean execute(Context context, File metaDirectory) {
        def c = executable.rehydrate(new StepDelegate(context, this), this, this)
        c.resolveStrategy = Closure.DELEGATE_ONLY
        c.call()
        return true
    }
}
