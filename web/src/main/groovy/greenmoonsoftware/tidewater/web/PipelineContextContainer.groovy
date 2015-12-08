package greenmoonsoftware.tidewater.web
import greenmoonsoftware.tidewater.context.ContextId

final class PipelineContextContainer {
    private final Map<String, ContextThread> runningThreads = [:]

    void add(String pipelineName, ContextId c, Thread t) {
        runningThreads.put(c.id, new ContextThread(pipelineName, c, t))
    }

    boolean abort(ContextId c) {
        throw new UnsupportedOperationException('Not yet supported')
    }

    void remove(ContextId c) {
        runningThreads.remove(c.id)
    }

    Collection<ContextThread> getThreads() {
        runningThreads.values().asImmutable()
    }

    static final class ContextThread {
        final String pipelineName
        final ContextId contextId
        final Thread thread

        ContextThread(String pipelineName, ContextId contextId, Thread thread) {
            this.pipelineName = pipelineName
            this.contextId = contextId
            this.thread = thread
        }
    }
}
