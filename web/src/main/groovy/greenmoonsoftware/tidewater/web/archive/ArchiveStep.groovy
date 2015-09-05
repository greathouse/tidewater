package greenmoonsoftware.tidewater.web.archive

class ArchiveStep {
    enum Outcome {
        NA('N/A', '¯\\_(ツ)_/¯', 'secondary', ''),
        SUCCESS('Success', '✓', 'success', ''),
        FAIL ('Failed', '✘', 'alert', 'active'),
        ERROR('Errored', '!', 'alert', 'active');

        String label
        String symbol
        String labelStyleClass
        String panelStyleClass

        private Outcome(String label, String symbol, String styleClass, String panelStyleClass) {
            this.label = label
            this.symbol = symbol
            this.labelStyleClass = styleClass
            this.panelStyleClass = panelStyleClass
        }
    }

    String name
    String type
    Outcome outcome = Outcome.NA
    List<String> logLines = []

    ArchiveStep(String name, String type) {
        this.name = name
        this.type = type
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
