package greenmoonsoftware.tidewater.web.archive

class StepAttempt {
    final int index
    ArchiveStep.Outcome outcome = ArchiveStep.Outcome.NA
    List<String> logLines = []
    List<Kv> attributes = []

    StepAttempt(int idx) {
        index = idx
    }
}
