package greenmoonsoftware.tidewater

import greenmoonsoftware.tidewater.step.AbstractStep
import groovy.transform.Canonical

@Canonical
class DummyStep extends AbstractStep {
    @Override
    boolean execute(Context context, File stepDirectory) {
        true
    }
}
