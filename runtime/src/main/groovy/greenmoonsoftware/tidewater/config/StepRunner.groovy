package greenmoonsoftware.tidewater.config

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
    @Delegate private final Context context
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
        raiseEvent(new StepStartedEvent(step, startDate))
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
        raiseEvent(new StepErroredEvent(step, endDate, Duration.between(startDate.toInstant(), endDate.toInstant()), e))
    }

    private boolean executeStep(Step step, Date startDate) {
        def success = step.execute(context, setupStepMetaDirectory(step))
        def endDate = new Date()
        if (success) {
            raiseEvent(new StepSuccessfullyCompletedEvent(step, endDate, Duration.between(startDate.toInstant(), endDate.toInstant())))
        } else {
            raiseEvent(new StepFailedEvent(step, endDate, Duration.between(startDate.toInstant(), endDate.toInstant())))
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
        raiseEvent(new StepConfiguredEvent(step))
        return step
    }
}
