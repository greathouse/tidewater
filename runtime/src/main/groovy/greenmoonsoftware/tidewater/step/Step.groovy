package greenmoonsoftware.tidewater.step

import greenmoonsoftware.tidewater.config.Context

interface Step extends Serializable {
    String getName()
    void setName(String name)
    boolean execute(Context context, File stepDirectory)

    Map<String, Object> getInputs()
    Map<String, Object> getOutputs()
}
