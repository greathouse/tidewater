package greenmoonsoftware.tidewater.web.context

enum PipelineContextStatus {
    IN_PROGRESS(0), ERROR(1), FAILURE(2), COMPLETE(3), ABORT(4), PAUSE(5)

    private static final fromValue = values().collectEntries{[ (it.value) : it]}
    static PipelineContextStatus fromValue(int value) { fromValue[value] }

    public final int value
    private PipelineContextStatus(int value) { this.value = value }
}
