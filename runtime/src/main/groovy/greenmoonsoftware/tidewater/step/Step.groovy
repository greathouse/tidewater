package greenmoonsoftware.tidewater.step

import greenmoonsoftware.tidewater.Context

interface Step extends Serializable {
    String getName()
    void setName(String name)
    StepResult execute(Context context, File stepDirectory)

    Map<String, Object> getInputs()
    Map<String, Object> getOutputs()
}
