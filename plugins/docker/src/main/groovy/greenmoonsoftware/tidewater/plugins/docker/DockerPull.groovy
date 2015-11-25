package greenmoonsoftware.tidewater.plugins.docker

import com.github.dockerjava.api.model.PullResponseItem
import com.github.dockerjava.core.command.PullImageResultCallback
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input

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
                .exec(new TidewaterLoggingResultCallback<PullImageResultCallback, PullResponseItem>(new PullImageResultCallback(), log))
                .awaitCompletion()
                .success
    }
}
`