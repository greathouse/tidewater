package greenmoonsoftware.tidewater.web.scripts.execute

import greenmoonsoftware.tidewater.step.Step

class StepSpec {
    String name
    Map<String, Object> inputs
    Map<String, Object> outputs

    StepSpec(Step s) {
        name = s.name
        inputs = s.inputs
        outputs = s.outputs
    }
}
