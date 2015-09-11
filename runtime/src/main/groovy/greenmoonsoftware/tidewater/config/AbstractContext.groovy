package greenmoonsoftware.tidewater.config

import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDefinition

abstract class AbstractContext implements Context {
    protected final definedSteps = [:] as LinkedHashMap<String, StepDefinition>
    protected final executedSteps = [:] as LinkedHashMap<String, Step>

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
}
