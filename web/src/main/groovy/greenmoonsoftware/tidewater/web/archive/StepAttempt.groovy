package greenmoonsoftware.tidewater.web.archive

class StepAttempt {
    final int index
    ArchiveStep.Outcome outcome = ArchiveStep.Outcome.NA
    List<String> logLines = []
    List<Kv> attributes = []
    String panelStyleClass = ''

    StepAttempt(int idx) {
        index = idx
    }

    String getPanelStyleClass() {
        panelStyleClass ?: outcome.panelStyleClass
    }
}
