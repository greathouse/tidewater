package greenmoonsoftware.tidewater.plugins.docker.pull
import com.github.dockerjava.api.model.PullResponseItem
import com.github.dockerjava.core.async.ResultCallbackTemplate

class TidewaterPullImageResultCallback extends ResultCallbackTemplate<TidewaterPullImageResultCallback, PullResponseItem> {
    private final Closure log
    boolean success = true

    TidewaterPullImageResultCallback(Closure log) {
        this.log = log
    }

    @Override
    void onComplete() {
        super.onComplete()
        log 'Completed'
    }

    @Override
    void onStart(Closeable stream) {
        log 'Starting...'
        super.onStart(stream)
    }

    @Override
    void onError(Throwable throwable) {
        success = false
        log stringifyThrowable(throwable)
    }

    @Override
    void onNext(PullResponseItem item) {
        log item.id, "${item.status} ${item.progress ?: ''}"
    }

    private StringWriter stringifyThrowable(Throwable throwable) {
        def sw = new StringWriter()
        def pw = new PrintWriter(sw)
        throwable.printStackTrace(pw)
        sw.toString()
    }
}
