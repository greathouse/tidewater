package greenmoonsoftware.sterling.config

interface Step {
    void execute(PrintStream log)
    Map<String, Object> getInputs()
    Map<String, Object> getOutputs()
}
