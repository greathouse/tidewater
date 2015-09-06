package greenmoonsoftware.tidewater.web.archive

class ArchiveStep {
    String name

    enum Outcome {
        NA('¯\\_(ツ)_/¯', '¯\\_(ツ)_/¯', 'warning', ''),
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

    String type
    List<StepAttempt> attempts = []

    ArchiveStep(String name, String type) {
        this.name = name
        this.type = type
    }

    void newAttempt() {
        attempts << new StepAttempt(attempts.size())
    }

    void addLog(String log) {
        attempts[-1].logLines << log
    }

    void addAttribute(String name, String value) {
        attempts[-1].attributes << new Kv(name, value)
    }

    void success() {
        attempts[-1].outcome = Outcome.SUCCESS
    }

    void failed() {
        attempts[-1].outcome = Outcome.FAIL
    }

    void errored() {
        attempts[-1].outcome = Outcome.ERROR
    }

    String getPanelStyleClass() {
        return attempts ? attempts[-1].outcome.panelStyleClass : Outcome.NA.panelStyleClass
    }
}
