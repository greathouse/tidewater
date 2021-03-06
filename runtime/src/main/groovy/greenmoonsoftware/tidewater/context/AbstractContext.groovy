package greenmoonsoftware.tidewater.context

import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDefinition

abstract class AbstractContext implements Context {
    private final Map<String, String> parameters = [:]
    private final ext = [:]
    protected final definedSteps = [:] as LinkedHashMap<String, StepDefinition>
    protected final executedSteps = [:] as LinkedHashMap<String, Step>

    @Override
    final String getParameter(String name) {
        parameters[name]
    }

    @Override
    final void setParameter(String name, String value) {
        parameters[name] = value
    }

    @Override
    final Map<String, String> getParameters() {
        parameters.asImmutable()
    }

    @Override
    final getExt(String name) {
        ext[name] ?: ''
    }

    @Override
    final void setExt(String name, def value) {
        ext[name] = value
    }

    @Override
    void addDefinedStep(StepDefinition stepDef) {
        if (definedSteps.containsKey(stepDef.name)) {
            throw new InvalidScriptException("Script contains two steps named '${stepDef.name}'.")
        }
        definedSteps[stepDef.name] = stepDef
    }

    @Override
    void addExecutedStep(Step s) {
        executedSteps[s.name] = s
    }

    @Deprecated
    Map<String, Step> getExecutedSteps() {
        executedSteps.asImmutable()
    }

    @Override
    Step findExecutedStep(String name) {
        executedSteps[name]
    }

    def methodMissing(String name, def value) {
        setExt(name, value[0])
    }

    def propertyMissing(String name) {
        getExt(name)
    }
}
