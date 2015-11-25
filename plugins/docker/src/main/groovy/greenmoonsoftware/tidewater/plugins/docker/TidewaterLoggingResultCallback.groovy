package greenmoonsoftware.tidewater.plugins.docker

import com.github.dockerjava.core.async.ResultCallbackTemplate

import java.util.concurrent.TimeUnit

class TidewaterLoggingResultCallback<C extends ResultCallbackTemplate, I> extends ResultCallbackTemplate<TidewaterLoggingResultCallback, I> {
    final C delegateCallback
    private final Closure log

    TidewaterLoggingResultCallback(C delegateCallback, Closure log) {
        this.delegateCallback = delegateCallback
        this.log = log
    }

    @Override
    void close() throws IOException {
        delegateCallback.close()
        super.close()
    }

    @Override
    TidewaterLoggingResultCallback awaitCompletion() throws InterruptedException {
        delegateCallback.awaitCompletion()
        super.awaitCompletion()
        return this
    }

    @Override
    TidewaterLoggingResultCallback awaitCompletion(long timeout, TimeUnit timeUnit) throws InterruptedException {
        delegateCallback.awaitCompletion(timeout, timeUnit)
        super.awaitCompletion(timeout, timeUnit)
        return this
    }

    @Override
    protected RuntimeException getFirstError() {
        return delegateCallback.getFirstError()
    }

    @Override
    void onComplete() {
        delegateCallback.onComplete()
        super.onComplete()
        log 'Completed'
    }

    @Override
    void onStart(Closeable stream) {
        log 'Starting...'
        delegateCallback.onStart(stream)
    }

    @Override
    void onError(Throwable throwable) {
        throwable.printStackTrace()
        log stringifyThrowable(throwable)
        try {
            delegateCallback.close()
            close();
        } catch (IOException e) {
            //ignore
        }
    }

    void onNext(I item) {
        delegateCallback.onNext(item)
    }

    private String stringifyThrowable(Throwable throwable) {
        def sw = new StringWriter()
        def pw = new PrintWriter(sw)
        throwable.printStackTrace(pw)
        sw.toString()
    }
}
