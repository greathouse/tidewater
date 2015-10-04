package greenmoonsoftware.tidewater.web

import greenmoonsoftware.tidewater.context.ContextId

import java.util.concurrent.ConcurrentHashMap

final class PipelineContextContainer {
    private final Map<ContextId, Thread> runningThreads = [:] as ConcurrentHashMap

    void add(ContextId c, Thread t) {
        runningThreads[c] = t
    }

    boolean abort(ContextId c) {
        throw new UnsupportedOperationException('Not yet supported')
    }

    void remove(ContextId c) {
        runningThreads.remove(c)
    }

    Collection<ContextId> getRunningContextIds() {
        runningThreads.keySet().asImmutable()
    }
}
