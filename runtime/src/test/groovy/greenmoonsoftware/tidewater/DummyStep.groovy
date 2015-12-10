package greenmoonsoftware.tidewater

import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.StepResult
import groovy.transform.Canonical

@Canonical
class DummyStep extends AbstractStep {
    @Override
    StepResult execute(Context context, File stepDirectory) {
        StepResult.SUCCESS
    }
}
