package greenmoonsoftware.sterling.config

interface Step {
    void execute(PrintStream log, File metaDirectory)
    Map<String, Object> getInputs()
    Map<String, Object> getOutputs()
}
