package greenmoonsoftware.tidewater.runtime

import greenmoonsoftware.tidewater.config.Context
import greenmoonsoftware.tidewater.config.step.CustomStep
import greenmoonsoftware.tidewater.config.step.StepConfiguration

abstract class TidewaterBaseScript extends Script {
    def step(StepConfiguration definition) {
        println "Step: ${definition.name}"
        Context.get().addStep(definition)
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
