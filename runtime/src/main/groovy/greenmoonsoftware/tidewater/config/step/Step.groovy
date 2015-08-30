package greenmoonsoftware.tidewater.config.step

import greenmoonsoftware.tidewater.config.Context

interface Step {
    String getName()
    void execute(Context context, File stepDirectory)

    void seralize(File toFile)
    void deserialize(File fromFile)
    Map<String, Object> getInputs()
    Map<String, Object> getOutputs()
}
