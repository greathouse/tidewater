package greenmoonsoftware.tidewater.step

enum StepResult {
    DISABLED(true),
    SUCCESS(true),
    FAILURE(false),
    ERROR(false),
    REQUIRE_MANUAL_APPROVAL(false);

    final boolean continueProcessing

    private StepResult(boolean continueProcessing) {
        this.continueProcessing = continueProcessing
    }

    static final from(Object b) {
        return b ? SUCCESS : FAILURE
    }
}