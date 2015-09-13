package greenmoonsoftware.tidewater.config

import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDefinition

abstract class AbstractContext implements Context {
    private final ext = [:]
    protected final definedSteps = [:] as LinkedHashMap<String, StepDefinition>
    protected final executedSteps = [:] as LinkedHashMap<String, Step>

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
