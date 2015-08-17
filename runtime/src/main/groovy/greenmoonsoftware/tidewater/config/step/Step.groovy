package greenmoonsoftware.tidewater.config.step

interface Step {
    void execute(PrintStream log, File metaDirectory)

    void seralize(File toFile)
    void unserialize(File fromFile)
    Map<String, Object> getInputs()
    Map<String, Object> getOutputs()
}
