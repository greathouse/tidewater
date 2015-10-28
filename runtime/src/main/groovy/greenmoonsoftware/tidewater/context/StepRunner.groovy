package greenmoonsoftware.tidewater.context

import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDefinition
import greenmoonsoftware.tidewater.step.StepDelegate
import greenmoonsoftware.tidewater.step.events.StepConfiguredEvent
import greenmoonsoftware.tidewater.step.events.StepErroredEvent
import greenmoonsoftware.tidewater.step.events.StepFailedEvent
import greenmoonsoftware.tidewater.step.events.StepStartedEvent
import greenmoonsoftware.tidewater.step.events.StepSuccessfullyCompletedEvent

import java.time.Duration

class StepRunner implements Serializable {
    private final Context context
    private final List<StepDefinition> steps

    StepRunner(Context c, List<StepDefinition> s) {
        context = c
        steps = s
    }

    void run() {
        for(defined in steps) {
            def success = start(configure(defined))
            if (!success) {
                break
            }
        }
    }

    private boolean start(Step step) {
        def startDate = new Date()
        context.raiseEvent(new StepStartedEvent(step, context.attributes.id, startDate))
        try {
            return executeStep(step, startDate)
        } catch (all) {
            all.printStackTrace()
            handleErroredStep(step, startDate, all)
            return false
        }
    }

    private StepErroredEvent handleErroredStep(Step step, Date startDate, Exception e) {
        def endDate = new Date()
        context.raiseEvent(new StepErroredEvent(step, context.attributes.id, endDate, Duration.between(startDate.toInstant(), endDate.toInstant()), e))
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
        def step = defined.type.newInstance() as Step
        step.name = defined.name
        def c = (Closure) defined.configureClosure.rehydrate(new StepDelegate(context, step), null, null)
        c.resolveStrategy = Closure.DELEGATE_ONLY
        c.call()
        context.raiseEvent(new StepConfiguredEvent(step, context.attributes.id))
        return step
    }
}
