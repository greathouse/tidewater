package greenmoonsoftware.tidewater.web.archive

import greenmoonsoftware.tidewater.step.Step

class ArchiveStep {
    enum Outcome { SUCCESS, FAIL, ERROR }

    String name
    String type
    Outcome outcome
    List<String> logLines = []

    ArchiveStep(Step s) {
        name = s.name
        type = s.class.simpleName
    }

    void addLog(String log) {
        logLines << log
    }

    void success() {
        outcome = Outcome.SUCCESS
    }

    void failed() {
        outcome = Outcome.FAIL
    }

    void errored() {
        outcome = Outcome.ERROR
    }
}
