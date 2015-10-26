package greenmoonsoftware.tidewater.web.context.view.details

import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.web.context.PipelineContextStatus

class PipelineContextViewDetails {
    String pipelineName
    String scriptText
    String workspace
    String metadataDirectory
    Date startTime
    Date endTime
    PipelineContextStatus status

    long getDuration() {
        endTime.time - startTime.time
    }

    private definedSteps = [:] as LinkedHashMap<String, DetailsStep>

    void defineStep(String stepName, String stepType) {
        if (!definedSteps.containsKey(stepName)) {
            definedSteps[stepName] = new DetailsStep(stepName, stepType)
        }
    }

    void stepStarted(String stepName, Date start) {
        definedSteps[stepName].attempts << new StepAttempt(start);
    }

    void log(String stepName, Date date, String message) {
        definedSteps[stepName].attempts[-1].logs << new StepLogMessage(date, message)
    }

    void stepSuccess(Step step, Date end) {
        stepEnded(step, end, Outcome.SUCCESS)
    }

    void stepFailed(Step step, Date end) {
        stepEnded(step, end, Outcome.FAIL)
    }

    void stepErrored(Step step, Date date, String stacktrace) {
        stepEnded(step, date, Outcome.ERROR).attempts[-1].logs << new StepLogMessage(date, stacktrace)
    }

    List<DetailsStep> getSteps() {
        definedSteps.values() as List
    }

    private DetailsStep stepEnded(Step step, Date end, Outcome outcome) {
        def d = definedSteps[step.name]
        d.attempts[-1].outcome = outcome
        d.attempts[-1].end = end
        def c = {
            d.attributes[it.key] = it.value.toString() ?: ''
        }
        step.inputs.each c
        step.outputs.each c
        return d
    }

    private class DetailsStep {
        String stepName
        String stepType
        List<StepAttempt> attempts = []
        Map<String, String> attributes = [:]


        DetailsStep(String stepName, String stepType) {
            this.stepName = stepName
            this.stepType = stepType
        }
    }

    private class StepAttempt {
        Date start
        Date end
        Outcome outcome = Outcome.NA
        List<StepLogMessage> logs = []

        StepAttempt(Date start) {
            this.start = start
        }

        long getDuration() {
            end.time - start.time
        }
    }

    private enum Outcome {
        NA, SUCCESS, FAIL, ERROR
    }

    private class StepLogMessage {
        Date dateTime
        String message

        StepLogMessage(Date dateTime, String message) {
            this.dateTime = dateTime
            this.message = message
        }
    }
}
