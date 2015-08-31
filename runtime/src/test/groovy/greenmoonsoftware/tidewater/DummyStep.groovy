package greenmoonsoftware.tidewater

import greenmoonsoftware.tidewater.config.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import groovy.transform.Canonical

@Canonical
class DummyStep extends AbstractStep {
    @Override
    void execute(Context context, File stepDirectory) {
    }
}
