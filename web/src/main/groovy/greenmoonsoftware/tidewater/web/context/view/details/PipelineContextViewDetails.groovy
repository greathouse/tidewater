package greenmoonsoftware.tidewater.web.context.view.details

import greenmoonsoftware.tidewater.step.Step

class PipelineContextViewDetails {
    String scriptText
    String workspace
    String metadataDirectory
    Date startTime

    private definedSteps = [:] as LinkedHashMap<String, DetailsStep>

    void defineStep(String stepName, String stepType) {
        if (!definedSteps.containsKey(stepName)) {
            definedSteps[stepName] = new DetailsStep(stepName, stepType)
        }
    }

    void stepStarted(String stepName) {
        definedSteps[stepName].attempts << new StepAttempt();
    }

    void log(String stepName, Date date, String message) {
        definedSteps[stepName].attempts[-1].logs << new StepLogMessage(date, message)
    }

    void stepSuccess(Step step) {
        stepEnded(step, Outcome.SUCCESS)
    }

    void stepFailed(Step step) {
        stepEnded(step, Outcome.FAIL)
    }

    void stepErrored(Step step, Date date, String stacktrace) {
        stepEnded(step, Outcome.ERROR).attempts[-1].logs << new StepLogMessage(date, stacktrace)
    }

    List<DetailsStep> getSteps() {
        definedSteps.values() as List
    }

    private DetailsStep stepEnded(Step step, Outcome outcome) {
        def d = definedSteps[step.name]
        d.attempts[-1].outcome = outcome
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
        Outcome outcome = Outcome.NA
        List<StepLogMessage> logs = []
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
