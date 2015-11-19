package greenmoonsoftware.tidewater.plugins.docker.pull
import com.github.dockerjava.api.model.PullResponseItem
import com.github.dockerjava.api.model.ResponseItem.ProgressDetail
import com.github.dockerjava.core.async.ResultCallbackTemplate

import java.text.DecimalFormat

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
        throwable.printStackTrace()
        success = false
        log stringifyThrowable(throwable)
        try {
            close();
        } catch (IOException e) {
            //ignore
        }
    }

    @Override
    void onNext(PullResponseItem item) {
        log item.id, "${item.status} ${item.progressDetail ? buildProgressBar(item) : ''}"
    }

    private String stringifyThrowable(Throwable throwable) {
        def sw = new StringWriter()
        def pw = new PrintWriter(sw)
        throwable.printStackTrace(pw)
        sw.toString()
    }

    private String buildProgressBar(PullResponseItem i) {
        return i.progress //Unfortunately the attributes in the progressDetails class are package scoped so we must use the deprecated method to get a progress bar.
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
