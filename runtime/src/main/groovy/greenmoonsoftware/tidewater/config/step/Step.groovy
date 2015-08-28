package greenmoonsoftware.tidewater.config.step

import greenmoonsoftware.tidewater.config.LogWriter

interface Step {
    String getName()
    void execute(LogWriter log, File metaDirectory)

    void seralize(File toFile)
    void deserialize(File fromFile)
    Map<String, Object> getInputs()
    Map<String, Object> getOutputs()
}
