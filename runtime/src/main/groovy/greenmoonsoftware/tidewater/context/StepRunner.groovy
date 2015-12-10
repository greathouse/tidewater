package greenmoonsoftware.tidewater.context

import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.plugins.PluginClassLoaderCache
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDefinition
import greenmoonsoftware.tidewater.step.StepDelegate
import greenmoonsoftware.tidewater.step.StepResult
import greenmoonsoftware.tidewater.step.events.StepConfiguredEvent
import greenmoonsoftware.tidewater.step.events.StepDisabledEvent
import greenmoonsoftware.tidewater.step.events.StepErroredEvent
import greenmoonsoftware.tidewater.step.events.StepFailedEvent
import greenmoonsoftware.tidewater.step.events.StepStartedEvent
import greenmoonsoftware.tidewater.step.events.StepSuccessfullyCompletedEvent
import groovy.transform.TailRecursive

import java.time.Duration

class StepRunner implements Serializable {
    private final Context context
    private final List<StepDefinition> steps

    StepRunner(Context c, List<StepDefinition> s) {
        context = c
        steps = s
    }

    long run() {
        process(steps)
    }

    @TailRecursive
    private long process(List<StepDefinition> remaining, long counter = 0) {
        return (remaining.size() == 0) ? counter
            : process(remaining.head()) ?
                process(remaining.tail(), counter + 1) : counter
    }

    private boolean process(StepDefinition defined) {
        return isDisabled(defined) ?: start(configure(defined)).continueProcessing
    }

    private boolean isDisabled(StepDefinition stepDefinition) {
        stepDefinition.enabled ? false : disabled(stepDefinition)
    }

    private boolean disabled(StepDefinition stepDefinition) {
        context.raiseEvent(new StepDisabledEvent(stepDefinition.name, context.attributes.id, new Date()))
        return true
    }

    private StepResult start(Step step) {
        def startDate = new Date()
        context.raiseEvent(new StepStartedEvent(step, context.attributes.id, startDate))
        def originalClassloader = Thread.currentThread().contextClassLoader
        try {
            Thread.currentThread().contextClassLoader = PluginClassLoaderCache.getFor(step.class)
            return executeStep(step, startDate) ? StepResult.SUCCESS : StepResult.FAILURE
        } catch (all) {
            return handleErroredStep(step, startDate, all)
        }
        finally {
            Thread.currentThread().contextClassLoader = originalClassloader
        }
    }

    private StepResult handleErroredStep(Step step, Date startDate, Exception e) {
        e.printStackTrace()
        def endDate = new Date()
        context.raiseEvent(new StepErroredEvent(step, context.attributes.id, endDate, Duration.between(startDate.toInstant(), endDate.toInstant()), e))
        return StepResult.ERROR
    }

    private boolean executeStep(Step step, Date startDate) {
        def success = step.execute(context, setupStepMetaDirectory(step))
        def endDate = new Date()
        if (success) {
            context.raiseEvent(new StepSuccessfullyCompletedEvent(step, context.attributes.id, endDate, Duration.between(startDate.toInstant(), endDate.toInstant())))
        } else {
            context.raiseEvent(new StepFailedEvent(step, context.attributes.id, endDate, Duration.between(startDate.toInstant(), endDate.toInstant())))
        }
        return success
    }

    private File setupStepMetaDirectory(Step step) {
        def stepDirectory = new File(context.metaDirectory, step.name)
        stepDirectory.mkdirs()
        return stepDirectory
    }

    private Step configure(StepDefinition defined) {
        def step = instantiateStep(defined.type)
        step.name = defined.name
        def c = (Closure) defined.configureClosure.rehydrate(new StepDelegate(context, step), null, null)
        c.resolveStrategy = Closure.DELEGATE_ONLY
        c.call()
        context.raiseEvent(new StepConfiguredEvent(step, context.attributes.id))
        return step
    }

    private Step instantiateStep(String type) {
        return locateClass(type).newInstance() as Step
    }

    private Class<?> locateClass(String type) {
        return PluginClassLoaderCache.getFor(type).loadClass(type)
    }
}
