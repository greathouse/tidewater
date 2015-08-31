package greenmoonsoftware.tidewater.runtime

import greenmoonsoftware.tidewater.step.CustomStep
import greenmoonsoftware.tidewater.step.StepConfiguration
import greenmoonsoftware.tidewater.step.StepConfiguredEvent

abstract class TidewaterBaseScript extends Script implements Serializable {
    def step(StepConfiguration definition) {
        this.binding.context.raiseEvent(new StepConfiguredEvent(definition))
    }

    def methodMissing(String name, args) {
        if (args.length == 1) {
            return customStep(name, args)
        }
        return stepConfiguration(name, args)
    }

    StepConfiguration stepConfiguration(String name, args) {
        def type = args[0].type
        def configureClosure = args[-1]
        return new StepConfiguration(name:name, type: type, configureClosure: configureClosure)
    }

    StepConfiguration customStep(String name, args) {
        Closure c = args[0]
        return new StepConfiguration(name: name, type: CustomStep, configureClosure: { executable c})
    }
}
