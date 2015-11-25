package greenmoonsoftware.tidewater.plugins.docker

import com.github.dockerjava.api.model.PullResponseItem
import com.github.dockerjava.api.model.ResponseItem
import com.github.dockerjava.core.command.PullImageResultCallback
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input

import java.text.DecimalFormat

import static greenmoonsoftware.tidewater.plugins.docker.Helper.dockerClient

class DockerPull extends AbstractStep {
    @Input String image
    @Input String uri = System.env['DOCKER_HOST']
    @Input String certPath = System.env['DOCKER_CERT_PATH']

    @Override
    boolean execute(Context context, File stepDirectory) {
        def log = context.&log.curry(this)

        return dockerClient(uri, certPath)
                .pullImageCmd(image)
                .exec(new Callback(log))
                .awaitCompletion()
                .success
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

    private class Callback extends TidewaterLoggingResultCallback<PullImageResultCallback, PullResponseItem> {
        private final Closure log
        boolean success = true

        Callback(Closure log) {
            super(new PullImageResultCallback(), log)
            this.log = log
        }

        @Override
        void onError(Throwable throwable) {
            super.onError(throwable)
            success = false
        }

        @Override
        void onNext(PullResponseItem item) {
            super.onNext(item)
            log item.id, "${item.status} ${item.progressDetail ? buildProgressBar(item) : ''}"
        }
    }
}