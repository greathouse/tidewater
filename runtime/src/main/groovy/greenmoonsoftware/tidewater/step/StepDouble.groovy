package greenmoonsoftware.tidewater.step

import greenmoonsoftware.tidewater.config.Context

class StepDouble implements Step {
    private static final String unsupportedExceptionMessage = 'This is a step double. This step is created due to serialization. You cannot execute deserialized steps.'
    private String name
    private Map<String, Object> inputs = [:]
    private Map<String, Object> outputs = [:]

    StepDouble(Step s) {
        name = s.name
        inputs = s.inputs
        outputs = s.outputs
    }

    @Override
    String getName() {
        name
    }

    @Override
    void setName(String name) {
        throw new RuntimeException(unsupportedExceptionMessage)
    }

    @Override
    boolean execute(Context context, File stepDirectory) {
        throw new RuntimeException(unsupportedExceptionMessage)
    }

    @Override
    Map<String, Object> getInputs() {
        inputs
    }

    @Override
    Map<String, Object> getOutputs() {
        outputs
    }

    def propertyMissing(String name) {
        inputs[name] ?: outputs[name]
    }
}
