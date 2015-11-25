package greenmoonsoftware.tidewater.plugins.docker

import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.model.ResponseItem
import com.github.dockerjava.core.async.ResultCallbackTemplate

import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

class TidewaterLoggingResultCallback<C extends ResultCallback, I extends ResponseItem> extends ResultCallbackTemplate<TidewaterLoggingResultCallback, I> {
    final C delegate
    private final Closure log
    boolean success = true

    TidewaterLoggingResultCallback(C delegate, Closure log) {
        this.delegate = delegate
        this.log = log
    }

    @Override
    void close() throws IOException {
        delegate.close()
        super.close()
    }

    @Override
    TidewaterLoggingResultCallback awaitCompletion() throws InterruptedException {
        delegate.awaitCompletion()
        super.awaitCompletion()
        return this
    }

    @Override
    TidewaterLoggingResultCallback awaitCompletion(long timeout, TimeUnit timeUnit) throws InterruptedException {
        delegate.awaitCompletion(timeout, timeUnit)
        super.awaitCompletion(timeout, timeUnit)
        return this
    }

    @Override
    protected RuntimeException getFirstError() {
        return delegate.getFirstError()
    }

    @Override
    void onComplete() {
        delegate.onComplete()
        super.onComplete()
        log 'Completed'
    }

    @Override
    void onStart(Closeable stream) {
        log 'Starting...'
        delegate.onStart(stream)
    }

    @Override
    void onError(Throwable throwable) {
        throwable.printStackTrace()
        success = false
        log stringifyThrowable(throwable)
        try {
            delegate.close()
            close();
        } catch (IOException e) {
            //ignore
        }
    }

    void onNext(I item) {
        delegate.onNext(item)
        log item.id, "${item.status} ${item.progressDetail ? buildProgressBar(item) : ''}"
    }

    private String stringifyThrowable(Throwable throwable) {
        def sw = new StringWriter()
        def pw = new PrintWriter(sw)
        throwable.printStackTrace(pw)
        sw.toString()
    }

    private String buildProgressBar(ResponseItem i) {
        return i.progress //Unfortunately the attributes in the progressDetails class are package scoped so we must use the deprecated method to getFor a progress bar.
//        def d = i.progressDetail
//        def percentageScale = (d.current / d.total) / 2
//        def bar = '' * percentageScale
//        bar = bar.padRight(50 - percentageScale, ' ')
//        return "[>${bar}]  ${byteSizeToHuman(d.current)}/${byteSizeToHuman(d.total)}"
    }

    private String byteSizeToHuman(long size) {
        if(size <= 0) return '0'
        final String[] units = [ 'B', 'kB', 'MB', 'GB', 'TB' ] as String[]
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024))
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups]
    }
}
