package greenmoonsoftware.tidewater.web
import greenmoonsoftware.tidewater.context.ContextId

final class PipelineContextContainer {
    private final Map<String, Thread> runningThreads = [:]

    void add(ContextId c, Thread t) {
        runningThreads.put(c.id, t)
    }

    boolean abort(ContextId c) {
        throw new UnsupportedOperationException('Not yet supported')
    }

    void remove(ContextId c) {
        runningThreads.remove(c.id)
    }

    Collection<ContextId> getRunningContextIds() {
        runningThreads.keySet().collect { new ContextId(it) }.asImmutable()
    }
}
