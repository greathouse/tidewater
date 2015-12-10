package greenmoonsoftware.tidewater.plugins.docker
import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.model.PushResponseItem
import com.github.dockerjava.core.command.PushImageResultCallback
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input
import greenmoonsoftware.tidewater.step.StepResult

class DockerPush extends AbstractStep {
    @Input String uri = System.env['DOCKER_HOST']
    @Input String certPath = System.env['DOCKER_CERT_PATH']
    @Input String image

    private Closure<Void> log
    private DockerClient docker

    @Override
    StepResult execute(Context context, File stepDirectory) {
        init(context)
        return StepResult.from(docker.pushImageCmd(image)
                .exec(new Callback(log))
                .awaitCompletion()
                .success)
    }

    private init(Context context) {
        log = context.&log.curry(this)
        docker = Client.dockerClient(uri, certPath)
    }

    private static class Callback extends TidewaterLoggingResultCallback<PushImageResultCallback, PushResponseItem> {
        private final Closure<Void> log
        boolean success = true

        Callback(Closure<Void> log) {
            super(new PushImageResultCallback(), log)
            this.log = log
        }

        @Override
        void onError(Throwable throwable) {
            super.onError(throwable)
            success = false
        }

        @Override
        void onNext(PushResponseItem item) {
            super.onNext(item)
            if (item.error) {
                logError(item)
            }
            else {
                logSuccess(item)
            }
        }

        private void logSuccess(PushResponseItem item) {
            log item.id, "${item.status} ${item.progress ?: ''}"
        }

        private void logError(PushResponseItem item) {
            log item.error
            success = false
        }
    }
}
